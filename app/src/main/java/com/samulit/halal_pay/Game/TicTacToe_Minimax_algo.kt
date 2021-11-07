package com.samulit.halal_pay.Game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.samulit.halal_pay.R
import com.samulit.tictoctoewithai.Board
import com.samulit.tictoctoewithai.Cell
import kotlinx.android.synthetic.main.activity_tic_tac_toe_minimax_algo.*

class TicTacToe_Minimax_algo : AppCompatActivity() {

    //Creating a 2D Array of ImageViews
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }

    //creating the board instance
    var board = Board()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe_minimax_algo)

        //calling the function to load our tic tac toe board
        loadBoard()

    }

    //function is mapping
    //the internal board to the ImageView array board
    private fun mapBoardToUi() {
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    Board.PLAYER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.fancing)
                        boardCells[i][j]?.setPadding(25,25,25,25)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.COMPUTER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.yinyang)
                        boardCells[i][j]?.setPadding(25,25,25,25)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardCells[i][j]?.setImageResource(0)
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }


    private fun loadBoard() {
        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = ImageView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 230
                    bottomMargin = 10
                    topMargin = 10
                    leftMargin = 10
                    rightMargin = 10
                }
                boardCells[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.red_light2))

                //attached a click listener to the board
                boardCells[i][j]?.setOnClickListener(CellClickListener(i, j))

                layout_board.addView(boardCells[i][j])
            }
        }
    }

    inner class CellClickListener(val i: Int, val j: Int) : View.OnClickListener {

        override fun onClick(p0: View?) {

            //checking if the game is not over
            if (!board.isGameOver) {

                //creating a new cell with the clicked index
                val cell = Cell(i, j)

                //placing the move for player
                board.placeMove(cell, Board.PLAYER)

                //calling minimax to calculate the computers move
                board.minimax(0, Board.COMPUTER)

                //performing the move for computer
                board.computersMove?.let {
                    board.placeMove(it, Board.COMPUTER)
                }

                //mapping the internal board to visual board
                mapBoardToUi()
            }

            //Displaying the results
            //according to the game status
            when {
/*                board.hasComputerWon() -> text_view_result.text = "Computer Won"
                board.hasPlayerWon() -> text_view_result.text = "You Won"
                board.isGameOver -> text_view_result.text = "Game Tied"*/
            }
        }
    }
}