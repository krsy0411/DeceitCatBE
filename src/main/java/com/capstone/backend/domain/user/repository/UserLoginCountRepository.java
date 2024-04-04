package com.capstone.backend.domain.user.repository;

import com.capstone.backend.domain.user.entity.User;
import com.capstone.backend.domain.user.entity.UserLoginCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.Optional;

public interface UserLoginCountRepository extends JpaRepository<UserLoginCount, Long> {

    default Optional<UserLoginCount> findByUserAndDate(User user, LocalDate date) {
        return findByUserAndLoginYearAndLoginWeek(user, date.get(IsoFields.WEEK_BASED_YEAR), date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
    }

    // 특정 사용자, 연도, 주에 대한 UserLoginCount 찾기
    Optional<UserLoginCount> findByUserAndLoginYearAndLoginWeek(User user, int year, int week);

    // 특정 사용자, 연도, 주에 대한 로그인 횟수 합계 조회
    @Query("SELECT SUM(ulc.count) FROM UserLoginCount ulc WHERE ulc.user.id = :userId AND ulc.loginYear = :year AND ulc.loginWeek = :week")
    Optional<Integer> sumLoginCountsByUserAndYearAndWeek(@Param("userId") long userId, @Param("year") int year, @Param("week") int week);


    // 전체 사용자, 연도, 주에 대한 로그인 횟수 합계 조회
    @Query("SELECT SUM(ulc.count) FROM UserLoginCount ulc WHERE ulc.loginYear = :year AND ulc.loginWeek = :week")
    Optional<Integer> sumTotalLoginCountsByYearAndWeek(@Param("year") int year, @Param("week") int week
    );

}
