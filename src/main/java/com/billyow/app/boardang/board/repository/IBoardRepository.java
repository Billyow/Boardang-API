package com.billyow.app.boardang.board.repository;

import com.billyow.app.boardang.board.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    //clears the cache and makes the db do other operations before doing this
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
DELETE FROM Board b
WHERE b.id = :boardId AND b.owner.id = :ownerId
""")
    Integer deleteByIdAndOwnerId(@Param("boardId") long boardId, @Param("ownerId") long ownerId);
}
