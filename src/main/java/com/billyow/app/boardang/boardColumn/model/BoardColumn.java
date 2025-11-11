package com.billyow.app.boardang.boardColumn.model;

import com.billyow.app.boardang.board.model.Board;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BoardColumn {
    @Id
    private long BoardColumnId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer position;
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
