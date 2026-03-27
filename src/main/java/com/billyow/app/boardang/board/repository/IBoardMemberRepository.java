package com.billyow.app.boardang.board.repository;

import com.billyow.app.boardang.board.model.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBoardMemberRepository extends JpaRepository<BoardMember, Long> {
   public Optional<BoardMember> findByUser_IdAndBoard_Id(Long userId, Long boardId);
   public Boolean existsByUser_IdAndBoard_Id(Long userId, Long boardId);
}
