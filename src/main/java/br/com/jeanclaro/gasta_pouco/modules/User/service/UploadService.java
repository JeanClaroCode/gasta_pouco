package br.com.jeanclaro.gasta_pouco.modules.User.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.jeanclaro.gasta_pouco.exceptions.UserNotFoundException;
import br.com.jeanclaro.gasta_pouco.modules.User.models.entity.UserEntity;
import br.com.jeanclaro.gasta_pouco.modules.User.repository.UserRepository;

@Service
public class UploadService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserRepository userRepository;

    public String uploadUserProfilePicture(UUID id, MultipartFile file) throws Exception {
        UserEntity user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException());

        String imageUrl = s3Service.uploadFile(id, file);
        user.setProfilePictureURL(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }
}
