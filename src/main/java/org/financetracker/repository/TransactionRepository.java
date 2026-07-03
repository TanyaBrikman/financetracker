package org.financetracker.repository;

import org.financetracker.entity.CategoryType;
import org.financetracker.entity.Transaction;
import org.financetracker.entity.TransactionType;
import org.financetracker.projection.BalanceResponseProjection;
import org.financetracker.projection.CategoryExpenseProjection;
import org.financetracker.projection.MonthlySummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
            SELECT
                        t FROM Transaction t WHERE
                        (:startDate IS NULL OR t.transactionDate >= :startDate) AND
                        (:endDate IS NULL OR t.transactionDate <= :endDate) AND
                        (:categoryType IS NULL OR t.categoryType = :categoryType) AND
                        (:type IS NULL OR t.type = :type)
            
            """)
    Page<Transaction> findAllTransactionWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("categoryType") CategoryType categoryType,
            @Param("type") TransactionType type,
            Pageable pageable
    );

    @Query("""
             SELECT
                         COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END), 0) as totalIncome,
                         COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END), 0) as totalExpense,
                         COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END), 0) -
                         COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END), 0) as balance
                         FROM Transaction t
                         WHERE
                                     t.user.id = :userId AND
                                     t.transactionDate >= :startDate AND
                                     t.transactionDate <= :endDate
            """)
    BalanceResponseProjection getBalance(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT
                        t.categoryType AS categoryType,
                        SUM(t.amount) AS totalAmount,
                        COUNT(t.id) AS transactionCount,
                        ROUND(COALESCE(SUM(t.amount) * 100.0 / SUM(SUM(t.amount)) OVER(), 0), 1) as percentage
            FROM Transaction t
            WHERE
                        t.user.id = :userId AND
                        t.type = 'EXPENSE'  AND
                        (t.transactionDate >= :startDate) AND
                        (t.transactionDate <= :endDate)
            GROUP BY t.categoryType
            ORDER BY SUM(t.amount) DESC
            """)
    List<CategoryExpenseProjection> getCategoryList(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT
                  EXTRACT(YEAR FROM t.transactionDate) AS year,
                  EXTRACT(MONTH FROM t.transactionDate) AS month,
                  SUM (t.amount) AS totalAmount,
                  CASE EXTRACT(MONTH FROM t.transactionDate)
                              WHEN 1 THEN 'Январь'
                              WHEN 2 THEN 'Февраль'
                              WHEN 3 THEN 'Март'
                              WHEN 4 THEN 'Апрель'
                              WHEN 5 THEN 'Май'
                              WHEN 6 THEN 'Июнь'
                              WHEN 7 THEN 'Июль'
                              WHEN 8 THEN 'Август'
                              WHEN 9 THEN 'Сентябрь'
                              WHEN 10 THEN 'Октябрь'
                              WHEN 11 THEN 'Ноябрь'
                              WHEN 12 THEN 'Декабрь'
                  END AS monthName,
                  COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END), 0) as totalIncome,
                  COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END), 0) as totalExpense,
                  COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END), 0) -
                  COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END), 0) as balance
            FROM Transaction t
            WHERE
                        t.user.id = :userId AND
                        EXTRACT(YEAR FROM t.transactionDate) = :year
            GROUP BY EXTRACT(YEAR FROM t.transactionDate), EXTRACT(MONTH FROM t.transactionDate)
            ORDER BY EXTRACT(YEAR FROM t.transactionDate) ASC
            """)
    List<MonthlySummaryProjection> getMonthlySummary(
            @Param("userId") Long userId,
            @Param("year") int year
    );
}