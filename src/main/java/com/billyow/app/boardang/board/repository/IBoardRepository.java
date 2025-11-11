package com.billyow.app.boardang.board.repository;

import com.billyow.app.boardang.board.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IBoardRepository extends JpaRepository<Board, Long> {
    @Query("""
SELECT DISTINCT b
FROM Board b
LEFT JOIN FETCH b.members m
WHERE b.owner.id= :userId OR m.id = :userId
""")
     List<Board> findAllBoardsFromUser_Id(@Param("userId") long UserId);
}
