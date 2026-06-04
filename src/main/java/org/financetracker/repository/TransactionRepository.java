package org.financetracker.repository;

import org.financetracker.entity.CategoryType;
import org.financetracker.entity.Transaction;
import org.financetracker.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:startDate IS NULL OR t.transactionDate >= :startDate) AND " +
            "(:endDate IS NULL OR t.transactionDate <= :endDate) AND " +
            "(:categoryType IS NULL OR t.categoryType = :categoryType) AND " +
            "(:type IS NULL OR t.type = :type)")
    Page<Transaction> findAllTransactionWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("categoryType") CategoryType categoryType,
            @Param("type") TransactionType type,
            Pageable pageable
    );

    default Page<Transaction> findAllWithFiltersSafe(
            LocalDate startDate,
            LocalDate endDate,
            CategoryType categoryType,
            TransactionType type,
            Pageable pageable
    ) {
        return findAllTransactionWithFilters(startDate, endDate, categoryType, type, pageable);
    }
}