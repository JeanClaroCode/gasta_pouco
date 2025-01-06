package br.com.jeanclaro.gasta_pouco.modules.Transaction.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import java.util.UUID;


public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    List<TransactionEntity> findByDescriptionContainingIgnoreCase(String description);
    List<TransactionEntity> findByAuthor(UUID id);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionEntity t WHERE t.author = :authorId AND t.type = 'INCOME'")
    BigDecimal findTotalIncomeByAuthor(@Param("authorId") UUID authorId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionEntity t WHERE t.author = :authorId AND t.type = 'EXPENSE'")
    BigDecimal findTotalExpenseByAuthor(@Param("authorId") UUID authorId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionEntity t WHERE t.author = :authorId")
    BigDecimal findTotalByAuthor(@Param("authorId") UUID authorId);

    List<TransactionEntity> findByDateBetweenAndAuthor(LocalDate startDate, LocalDate endDate, UUID authorId);

    // @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionEntity t " +
    //     "AND t.date >= :startDate AND t.date < :endDate" +
    //     "WHERE t.author = :authorId AND t.type = 'income' ")
    // BigDecimal findTotalIncomeByMonthAndYear(
    //     @Param("startDate") LocalDate startDate,
    //     @Param("endDate") LocalDate endDate,
    //     @Param("authorId") UUID authorId);

    // @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionEntity t " +
    //     "AND t.date >= :startDate AND t.date < :endDate" +
    //     "WHERE t.author = :authorId AND t.type = 'expense' ")
    // BigDecimal findTotalExpenseByMonthAndYear(
    //     @Param("startDate") LocalDate startDate,
    //     @Param("endDate") LocalDate endDate,
    //     @Param("authorId") UUID authorId);
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionEntity t " +
        "WHERE t.date >= :startDate AND t.date < :endDate " +
        "AND t.author = :authorId AND t.type = 'INCOME'")
    BigDecimal findTotalIncomeByMonthAndYear(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("authorId") UUID authorId);

        @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionEntity t " +
        "WHERE t.date >= :startDate AND t.date < :endDate " +
        "AND t.author = :authorId AND t.type = 'EXPENSE'")
        BigDecimal findTotalExpenseByMonthAndYear(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("authorId") UUID authorId);
}
