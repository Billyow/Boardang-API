package com.billyow.app.boardang.boardColumn.repository;
import com.billyow.app.boardang.boardColumn.model.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IBoardColumn extends JpaRepository<BoardColumn, Long> {
     List<BoardColumn> findByBoard_IdOrderByPositionAsc(Long boardId);
}
