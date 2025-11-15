package com.billyow.app.boardang.boardColumn.repository;
import com.billyow.app.boardang.boardColumn.model.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface IBoardColumnRepository extends JpaRepository<BoardColumn, Long> {
     List<BoardColumn> findByBoard_IdOrderByPositionAsc(Long boardId);
     void deleteByBoard_Id(Long boardId);
     @Query("""
    SELECT MAX(c.position) FROM BoardColumn c
    WHERE c.board.id = :boardId
""")
     int getMaxPositionByBoardId(@Param("boardId")Long boardId);
}
