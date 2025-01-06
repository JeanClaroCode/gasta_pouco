package br.com.jeanclaro.gasta_pouco.modules.User.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.jeanclaro.gasta_pouco.modules.User.models.dto.AuthUserRequestDTO;
import br.com.jeanclaro.gasta_pouco.modules.User.models.dto.EditUserRequestDTO;
import br.com.jeanclaro.gasta_pouco.modules.User.models.entity.UserEntity;
import br.com.jeanclaro.gasta_pouco.modules.User.service.AuthUserUseCase;
import br.com.jeanclaro.gasta_pouco.modules.User.service.CreateUserUseCase;
import br.com.jeanclaro.gasta_pouco.modules.User.service.DeleteUserUseCase;
import br.com.jeanclaro.gasta_pouco.modules.User.service.EditUserUseCase;
import br.com.jeanclaro.gasta_pouco.modules.User.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Tag(name = "Usuário", description = "Gerenciamento de usuários")
public class UserController {

    @Autowired
    CreateUserUseCase createUserUseCase;

    @Autowired 
    AuthUserUseCase authUserUseCase;

    @Autowired
    EditUserUseCase editUserUseCase;

    @Autowired
    DeleteUserUseCase deleteUserUseCase;

    @Autowired
    private UploadService uploadService;

    @PostMapping("/register")

    @Operation(summary = "Criar Usuário", description = "Essa função cria um usuário.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso", content = {
            @Content(schema = @Schema(implementation = UserEntity.class))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao criar usuário")
    })

    public ResponseEntity<Object> create(@Valid @RequestBody UserEntity createUserUseCase){
        try {
            var result = this.createUserUseCase.execute(createUserUseCase);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sessão", description = "Essa função autentica um usuário existente e retorna um token.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Erro ao logar")
    })

    public ResponseEntity<Object> auth(@RequestBody AuthUserRequestDTO authUserRequestDTO){
        try {
            var token = this.authUserUseCase.execute(authUserRequestDTO);
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")

    @Operation(summary = "Editar usuário", description = "Essa função edita um usuário.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário editado com sucesso", content = {
            @Content(schema = @Schema(implementation = UserEntity.class))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao editar usuário")
    })
    @SecurityRequirement(name = "jwt_auth")
    public UserEntity alterar(@PathVariable ("id") UUID id, @RequestBody EditUserRequestDTO editUserRequestDTO){
        return editUserUseCase.execute(id, editUserRequestDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")

    @Operation(summary = "Deletar usuário", description = "Essa função deleta um usuário.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao deletar usuário")
    })
    @SecurityRequirement(name = "jwt_auth")
    public void excluir(HttpServletRequest request){
        var idCandidate = request.getAttribute("user_id");
        var idConverted = UUID.fromString(idCandidate.toString());  
        deleteUserUseCase.execute(idConverted);
    }

    @PostMapping("/img")
    @PreAuthorize("hasRole('USER')")

    @Operation(summary = "Mudar foto de perfil", description = "Essa função altera a foto de perfil do usuário.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Imagem enviada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao enviar imagem")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file, HttpServletRequest request){

        var idCandidate = request.getAttribute("user_id");
        var idConverted = UUID.fromString(idCandidate.toString());      
        
        try {
                String imageUrl = uploadService.uploadUserProfilePicture(idConverted, file);
                return ResponseEntity.ok().body("Imagem enviada com sucesso! URL: " + imageUrl);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
}
