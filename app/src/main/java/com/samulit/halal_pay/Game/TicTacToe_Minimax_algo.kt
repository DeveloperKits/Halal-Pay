package com.samulit.halal_pay.Game

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.samulit.halal_pay.Model.MediaSound
import com.samulit.halal_pay.R
import com.samulit.tictoctoewithai.Board
import com.samulit.tictoctoewithai.Cell
import kotlinx.android.synthetic.main.activity_tic_tac_toe_minimax_algo.*
import pl.droidsonroids.gif.GifImageView

class TicTacToe_Minimax_algo : AppCompatActivity() {

    //Creating a 2D Array of ImageViews
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }
    private var media = MediaSound();

    //creating the board instance
    var board = Board()

    private var count: Int? = 0
    private var entryFee: Int? = 0
    private var computerwincount: Int? = 0
    private var yourwincount: Int? = 0
    private var userRef: DatabaseReference? = null
    private var userID: String? = null
    private var imageHint: String? = null
    private var ishard: String? = null
    private var name: String? = null
    private var userCoin: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe_minimax_algo)

        //calling the function to load our tic tac toe board
        loadBoard()

        // Game Field
        userID = FirebaseAuth.getInstance().uid
        userRef = FirebaseDatabase.getInstance().getReference("Game").child(userID!!)
        userRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                count = snapshot.child("Count").getValue(Int::class.java)
                entryFee = snapshot.child("Entry Fee").getValue(Int::class.java)
                imageHint = snapshot.child("Piece Image").getValue(String::class.java)
                ishard = snapshot.child("isHard").getValue(String::class.java)
                name = snapshot.child("name").getValue(String::class.java)
                computerwincount = snapshot.child("computer win").getValue(Int::class.java)
                yourwincount = snapshot.child("you win").getValue(Int::class.java)

                opponent_name.text = name

                if (count == 1) {
                    f1.setImageResource(R.drawable.ic_baseline_favorite_24)
                    f2.setImageResource(R.drawable.ic_baseline_favorite_24)
                    f3.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else if (count == 2) {
                    f1.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    f2.setImageResource(R.drawable.ic_baseline_favorite_24)
                    f3.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    f1.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    f2.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    f3.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        userRef = FirebaseDatabase.getInstance().getReference("UserData").child(userID!!)
        userRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userCoin = snapshot.child("UserCoin").getValue(Int::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        back.setOnClickListener {
            val dialog: AlertDialog = AlertDialog.Builder(this)
                    .setMessage("Are you sure! Are you ready to lose the game?")
                    .setPositiveButton("yes") { dialogInterface, i ->
                        onBackPressed()
                        userRef = FirebaseDatabase.getInstance().getReference("UserData").child(userID!!)
                        userRef!!.child("UserCoin").setValue(userCoin?.minus(entryFee!!)) }
                    .setNegativeButton("no", null)
                    .create()
            dialog.show()
        }

    }

    //function is mapping
    //the internal board to the ImageView array board
    private fun mapBoardToUi() {
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    Board.PLAYER -> {
                        // For click sound
                        media.buttonSound(this)
                        boardCells[i][j]?.setImageResource(R.drawable.fancing)
                        boardCells[i][j]?.setPadding(25, 25, 25, 25)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.COMPUTER -> {
                        // For click sound
                        Handler(Looper.getMainLooper()).postDelayed({
                            boardCells[i][j]?.setImageResource(R.drawable.yinyang)
                            boardCells[i][j]?.setPadding(25, 25, 25, 25)
                            boardCells[i][j]?.isEnabled = false
                            media.buttonSound(this)
                        }, 700)

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

                board.hasComputerWon() -> {
                    Toast.makeText(this@TicTacToe_Minimax_algo, "Computer Won", Toast.LENGTH_SHORT).show()
                    dialog("Computer Won")
                }
                board.hasPlayerWon() -> {
                    Toast.makeText(this@TicTacToe_Minimax_algo, "You Won", Toast.LENGTH_SHORT).show()
                    dialog("You Won")
                }
                board.isGameOver -> {
                    Toast.makeText(this@TicTacToe_Minimax_algo, "Game Tied", Toast.LENGTH_SHORT).show()
                    dialog("Game Tied")
                }

            }
        }
    }

    // show dialog
    private fun dialog(titles: String){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.in_game_dalog)

        val winning = dialog.findViewById(R.id.winning) as GifImageView
        val winning2 = dialog.findViewById(R.id.winning2) as GifImageView
        val cancel = dialog.findViewById(R.id.cancel) as Button
        val done = dialog.findViewById(R.id.button) as Button
        val textWin = dialog.findViewById(R.id.text_win) as TextView
        val youWin = dialog.findViewById(R.id.you_win) as ImageView

        textWin.text = titles
        if (count == 2){
            done.text = "3rd Time"
        }else if (count == 3){
            done.text = "Back Home"
        }

        count = count?.plus(1)
        userRef = FirebaseDatabase.getInstance().getReference("Game").child(userID!!)
        userRef!!.child("Count").setValue(count)

        if (titles == "Computer Won"){
            winning.visibility = View.INVISIBLE
            winning2.visibility = View.INVISIBLE
            youWin.setImageResource(R.drawable.you_lost)

            computerwincount = computerwincount?.plus(1)
            userRef!!.child("computer win").setValue(computerwincount)
        }else if(titles == "You Won"){
            winning.visibility = View.VISIBLE
            winning2.visibility = View.VISIBLE
            youWin.setImageResource(R.drawable.you_win)

            yourwincount = yourwincount?.plus(1)
            userRef!!.child("you win").setValue(yourwincount)
        }else{
            winning.visibility = View.INVISIBLE
            winning2.visibility = View.INVISIBLE
            youWin.setImageResource(R.drawable.tied)
        }

        cancel.setOnClickListener { dialog.dismiss() }

        done.setOnClickListener {
            if (count == 4){
                userRef = FirebaseDatabase.getInstance().getReference("Game").child(userID!!)
                userRef!!.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        computerwincount = snapshot.child("computer win").getValue(Int::class.java)
                        yourwincount = snapshot.child("you win").getValue(Int::class.java)

                        if (yourwincount!! > computerwincount!!) {
                            userRef = FirebaseDatabase.getInstance().getReference("UserData").child(userID!!)
                            userRef!!.child("UserCoin").setValue(userCoin?.plus((entryFee?.times(1.5)!!)))
                        } else if (yourwincount!! < computerwincount!!) {
                            userRef = FirebaseDatabase.getInstance().getReference("UserData").child(userID!!)
                            userRef!!.child("UserCoin").setValue(userCoin?.minus(entryFee!!))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })

                dialog.dismiss()
                val intent = Intent(this, GameHome::class.java)
                startActivity(intent)
                finish()

            }else {
                dialog.dismiss()
                val intent = Intent(this, TicTacToe_Minimax_algo::class.java)
                startActivity(intent)
                finish()
            }
        }

        dialog.show()
    }
}
