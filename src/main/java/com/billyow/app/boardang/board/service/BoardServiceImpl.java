package com.billyow.app.boardang.board.service;

import com.billyow.app.boardang.auth.service.AuthService;
import com.billyow.app.boardang.board.DTO.CreateBoardRequest;
import com.billyow.app.boardang.board.model.Board;
import com.billyow.app.boardang.board.repository.IBoardRepository;
import com.billyow.app.boardang.boardColumn.repository.IBoardColumnRepository;
import com.billyow.app.boardang.task.repository.ITaskRepository;
import com.billyow.app.boardang.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements IBoardService {
    private final IBoardRepository boardRepository;
    private final AuthService authService;
    private final ITaskRepository taskRepository;
    private final IBoardColumnRepository columnRepository;
    private final IUserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Board> getUserBoards(Long userId) {
        return boardRepository.findAllBoardsFromUser_Id(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
    }

    @Transactional
    @Override
    public Board createBoard(CreateBoardRequest request) {
        var newBoard = new Board();
        newBoard.setTitle(request.title());
        newBoard.setDescription(request.description());
        var currentUserId = authService.getCurrentUserId();
        var owner = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        newBoard.setOwner(owner);
        return boardRepository.save(newBoard);
    }

    @Transactional
    @Override
    public void deleteBoard(Long boardId) {
        Long currentUserId = authService.getCurrentUserId();
        var affected = boardRepository.deleteByIdAndOwnerId(boardId,currentUserId);
        if(affected==1){
            try{
                columnRepository.deleteByBoard_Id(boardId);
                taskRepository.deleteTaskByBoardId(boardId);
            }catch (Exception e){
                throw new RuntimeException("Error deleting tasks or columns",e);
            }
            return;
        }
        // differentiate 403 from 404
        if(boardRepository.existsById(boardId)){
            throw new RuntimeException("User is not the owner of this board");
        }
        throw new RuntimeException("Board not found");
    }


}
