package com.example.quoridor

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.customView.WallSelectorView
import com.example.quoridor.customView.gameBoardView.Board
import com.example.quoridor.customView.gameBoardView.GameBoardViewDropListener
import com.example.quoridor.customView.gameBoardView.GameBoardViewPieceClickListener
import com.example.quoridor.customView.gameBoardView.GameBoardViewPlayerImageGetter
import com.example.quoridor.customView.gameBoardView.Wall
import com.example.quoridor.customView.gameBoardView.WallType
import com.example.quoridor.customView.playerView.Player
import com.example.quoridor.databinding.ActivityGameForPvpBinding
import com.example.quoridor.game.Notation
import com.example.quoridor.game.types.GameType
import com.example.quoridor.game.types.NotationType
import com.example.quoridor.game.util.GameFunc
import com.example.quoridor.util.Coordinate
import com.example.quoridor.util.Func.get
import com.example.quoridor.util.Func.popToast
import com.example.quoridor.util.Func.set
import com.example.quoridor.util.Func.setSize
import java.util.Timer
import kotlin.concurrent.timer

class GameForPvPActivity:  AppCompatActivity() {
    private var available: Array<Coordinate> = Array(0){ Coordinate(0,0) }

    private val binding: ActivityGameForPvpBinding by lazy {
        ActivityGameForPvpBinding.inflate(layoutInflater)
    }
    private val playersData by lazy {
        arrayOf(binding.myInfoView.data, binding.opPlayerInfoView.data)
    }
    private val boardData by lazy {
        binding.gameBoardView.data
    }

    private val initWall = 10
    private val timeLimit by lazy {
        when(intent.getIntExtra("gameType", GameType.BLITZ.ordinal)) {
            GameType.BLITZ.ordinal -> GameType.BLITZ.timeLimit
            GameType.STANDARD.ordinal -> GameType.STANDARD.timeLimit
            GameType.CLASSIC.ordinal -> GameType.CLASSIC.timeLimit
            else -> GameType.BLITZ.timeLimit
        }
    }
    private val imageResourceList = arrayOf( R.drawable.baseline_lens_24_red, R.drawable.baseline_lens_24_blue)
    private val imageViewList by lazy {
        Array(2) { ImageView(applicationContext) }
    }
    private var timer: Timer? = null
    private var turn = 0

    private val TAG by lazy {
        applicationContext.getString(R.string.Dirtfy_test_tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val gameType = intent.getIntExtra("gameType", GameType.BLITZ.ordinal)

        binding.gameBoardView.walls[WallType.VERTICAL.ordinal][0][0].post {
            val vertWalls = binding.gameBoardView.walls[WallType.VERTICAL.ordinal]
            val short = vertWalls[0][0].width
            val long = vertWalls[0][0].height

            binding.myWallSelector.verticalWallView.setSize(short, long)
            binding.myWallSelector.horizontalWallView.setSize(long, short)
        }

        binding.gameBoardView.setDropListener(object : GameBoardViewDropListener {
            override fun cross(matchedView: View, wall: Wall): Boolean {
                if (GameFunc.wallCross(wall, boardData.value!!)) {
                    popToast(this@GameForPvPActivity, "cross")
                    return true
                }
                return false
            }

            override fun closed(matchedView: View, wall: Wall): Boolean {
                if (GameFunc.wallClosed(wall, boardData.value!!)) {
                    popToast(this@GameForPvPActivity, "closed")
                    return true
                }
                return false
            }

            override fun match(matchedView: View, wall: Wall): Boolean {
                if (GameFunc.wallMatch(wall, boardData.value!!)) {
                    popToast(this@GameForPvPActivity, "match")
                    return true
                }
                return false
            }

            override fun success(matchedView: View, wall: Wall) {
                val notationType = when(wall.type) {
                    WallType.VERTICAL -> NotationType.VERTICAL
                    WallType.HORIZONTAL -> NotationType.HORIZONTAL
                }
                val notation = Notation(notationType, wall.coordinate)
                turnPass(notation)
            }
        })
        binding.gameBoardView.setClickListener(object: GameBoardViewPieceClickListener {
            override fun click(clickedPiece: View, row: Int, col: Int) {
                val cor = Coordinate(row, col)

                if(cor in available){
                    turnPass(Notation(NotationType.MOVE, cor))
                }
                else{
                    Toast.makeText(applicationContext,R.string.Cannot_Go, Toast.LENGTH_LONG).show()
                }
            }
        })
        binding.gameBoardView.setImageGetter(object : GameBoardViewPlayerImageGetter {
            override fun getImageView(playerNum: Int): ImageView {
                return imageViewList[playerNum]
            }
        })

        initGame()
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }

    private fun removePlayerShadow() {
        for(a in available){ //view 지우기
            binding.gameBoardView.pieces.get(a).removeAllViews()
        }
    }
    private fun addPlayerShadow(playerNum: Int){
        if (boardData.value == null)
            return

        val boardValue = boardData.value!!
        available = GameFunc.getAvailableMove(playerNum, boardValue)

        for(a in available){
            val playerShadow = createPlayerImageView(imageResourceList[playerNum])
            playerShadow.alpha = 0.5f
            binding.gameBoardView.pieces.get(a).addView(playerShadow)
        }
    }
    private fun setWallChooseView(selector: WallSelectorView) {
        binding.gameBoardView.setWallChooseView(
            selector.verticalWallChooseView,
            selector.horizontalWallChooseView)
    }
    private fun createPlayerImageView(imageId: Int): ImageView {
        val image = ImageView(applicationContext)
        image.setImageResource(imageId)
        image.adjustViewBounds = true
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        image.layoutParams = lp
        return image
    }

    private fun buildTimer(playerNum: Int, timeOver: () -> Unit = {}): Timer? {
        if (playersData[playerNum].value == null)
            return null

        val playerValue = playersData[playerNum].value!!
        return timer(period = 1000L) {
            if (playerValue.leftTime <= 0L) {
                this@GameForPvPActivity.runOnUiThread {
                    timeOver()
                }
                this.cancel()
            }

            playerValue.leftTime -= 1000L

            playersData[playerNum].postValue(playerValue)

            Log.d(TAG, "timer is running ${playerValue.leftTime}")
        }
    }

    fun initGame() {
        val player0 = Player("p0", timeLimit, initWall, 1050)
        binding.myInfoView.profileImageView.setImageResource(R.drawable.hobanwoo_red)
        binding.myInfoView.data.value = player0
        val player1 = Player("p1", timeLimit, initWall, 950)
        binding.opPlayerInfoView.profileImageView.setImageResource(R.drawable.hobanwoo_blue)
        binding.opPlayerInfoView.data.value = player1

        val board = Board()
        binding.gameBoardView.data.value = board

        imageViewList[0] = createPlayerImageView(imageResourceList[0])
        imageViewList[1] = createPlayerImageView(imageResourceList[1])

        setWallChooseView(binding.myWallSelector)
        addPlayerShadow(0)

        timer = buildTimer(0) {
            popToast(this, "p0 time over")
        }
    }

    fun turnPass(notation: Notation) {
        timer?.cancel()

        removePlayerShadow()

        val playerValue = playersData[turn].value!!
        val boardValue = boardData.value!!
        when(notation.type) {
            NotationType.MOVE -> {
                boardValue.playCoordinates[turn] = notation.coordinate
            }
            NotationType.VERTICAL -> {
                boardValue.verticalWalls.set(notation.coordinate, true)
                playerValue.leftWall -= 1
            }
            NotationType.HORIZONTAL -> {
                boardValue.horizontalWalls.set(notation.coordinate, true)
                playerValue.leftWall -= 1
            }
        }
        playersData[turn].value = playerValue
        boardData.value = boardValue

        turn = (turn+1)%2

        addPlayerShadow(turn)

        timer = buildTimer(turn) {
            popToast(this, "p$turn time over")
        }
    }
}