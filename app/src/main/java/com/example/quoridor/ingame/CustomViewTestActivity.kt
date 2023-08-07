package com.example.quoridor.ingame

import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.quoridor.R
import com.example.quoridor.databinding.ActivityCustomViewTestBinding
import com.example.quoridor.domain.Notation
import com.example.quoridor.domain.Player
import com.example.quoridor.domain.utils.NotationType
import com.example.quoridor.domain.utils.PlayerColorType
import com.example.quoridor.ingame.customView.GameBoardViewDropListener
import com.example.quoridor.ingame.customView.GameBoardViewPieceClickListener
import com.example.quoridor.ingame.frontDomain.FrontBoard
import com.example.quoridor.ingame.frontDomain.FrontPlayer
import com.example.quoridor.ingame.utils.DropReturnType
import com.example.quoridor.ingame.utils.Func
import com.example.quoridor.ingame.utils.TimeCounter
import com.example.quoridor.ingame.utils.TimeCounterOverListener
import com.example.quoridor.ingame.utils.WallType

class CustomViewTestActivity : ComponentActivity() {

    private val binding: ActivityCustomViewTestBinding by lazy {
        ActivityCustomViewTestBinding.inflate(layoutInflater)
    }

    private lateinit var board: FrontBoard

    private val TAG by lazy {
        applicationContext.getString(R.string.Dirtfy_test_tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.gameBoardView.walls[WallType.Vertical.ordinal][0][0].post {
            val vertWalls = binding.gameBoardView.walls[WallType.Vertical.ordinal]
            val short = vertWalls[0][0].width
            val long = vertWalls[0][0].height

            var lp = binding.vertWall.layoutParams
            lp.width = short
            lp.height = long
            binding.vertWall.layoutParams = lp
            Log.d(TAG, "vw w:${binding.vertWall.width}, h:${binding.vertWall.height}")

            lp = binding.horiWall.layoutParams
            lp.width = long
            lp.height = short
            binding.horiWall.layoutParams = lp
        }

        binding.gameBoardView.setWallChooseView(binding.vertWallLayout, binding.horiWallLayout)
        binding.gameBoardView.setDragListener(object: GameBoardViewDropListener{
            override fun drop(matchedView: View, wallType: WallType, row: Int, col: Int): DropReturnType {
                val notationType = when(wallType) {
                    WallType.Vertical -> NotationType.VERTICAL
                    WallType.Horizontal -> NotationType.HORIZONTAL
                }

                val notation = Notation(notationType, row, col)

                return if (Func.wallCross(notation, board) || Func.wallClosed(notation, board)){
                    Toast.makeText(applicationContext, R.string.Cross_Wall, Toast.LENGTH_SHORT).show()
                    if(board.walls[wallType.ordinal][row][col]) DropReturnType.Match
                    else DropReturnType.Cross
                }
                else {
                    board.doNotation(notation)
                    DropReturnType.None
                }
            }
        })
        binding.gameBoardView.setClickListener(object: GameBoardViewPieceClickListener {
            override fun click(clickedPiece: View, row: Int, col: Int) {
                val p = board.nowPlayer()
                binding.gameBoardView.pieces[p.row][p.col].removeView(p.imageView)
                binding.gameBoardView.pieces[row][col].addView(p.imageView)

                val notation = Notation(NotationType.PLAYER, row, col)
                board.doNotation(notation)
            }
        })

        setGame()

        board.start()
    }

    private fun createPlayerImageView(imageId: Int): ImageView{
        val image = ImageView(applicationContext)
        image.setImageResource(imageId)
        image.adjustViewBounds = true
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        image.layoutParams = lp
        return image
    }

    private fun setGame(){
        val leftWall = 10
        val timeLimit: Long = 60 * 1000

        val player0 = FrontPlayer(
            createPlayerImageView(R.drawable.hobanwoo_red),
            TimeCounter(this, binding.p0Timer, timeLimit, object : TimeCounterOverListener {
                override fun timeOver() {
                    Toast.makeText(applicationContext, "p0 time over", Toast.LENGTH_SHORT).show()
                }
            }),
            Player(leftWall, timeLimit, 8, 4, "p0", PlayerColorType.RED)
        )

        val player1 = FrontPlayer(
            createPlayerImageView(R.drawable.hobanwoo_blue),
            TimeCounter(this, binding.p1Timer, timeLimit, object : TimeCounterOverListener {
                override fun timeOver() {
                    Toast.makeText(applicationContext, "p1 time over", Toast.LENGTH_SHORT).show()
                }
            }),
            Player(leftWall, timeLimit, 0, 4, "p1", PlayerColorType.BLUE)
        )

        val players = arrayOf(player0, player1)
        val verticalWalls = ArrayList<Pair<Int, Int>>()
        val horizontalWalls = ArrayList<Pair<Int, Int>>()

        board = FrontBoard(players, verticalWalls, horizontalWalls, true)

        binding.gameBoardView.setBoard(board)
    }
}