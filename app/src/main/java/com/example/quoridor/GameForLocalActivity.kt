package com.example.quoridor

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.customView.WallSelectorView
import com.example.quoridor.databinding.ActivityGameForLocalBinding
import com.example.quoridor.customView.gameBoardView.GameBoardViewDropListener
import com.example.quoridor.customView.gameBoardView.GameBoardViewPieceClickListener
import com.example.quoridor.customView.playerView.MyPlayInfoView
import com.example.quoridor.game.Board
import com.example.quoridor.game.LocalPlayer
import com.example.quoridor.game.Notation
import com.example.quoridor.game.Referee
import com.example.quoridor.util.Coordinate
import com.example.quoridor.game.util.GameFunc
import com.example.quoridor.game.util.timeCounter.TimeCounter
import com.example.quoridor.game.util.timeCounter.TimeCounterOverListener
import com.example.quoridor.game.util.types.DropReturnType
import com.example.quoridor.game.util.types.NotationType
import com.example.quoridor.game.util.types.WallType
import com.example.quoridor.util.Func.millToMinSec
import com.example.quoridor.util.Func.setSize
import com.example.quoridor.util.Func.get

class GameForLocalActivity:  AppCompatActivity(), Referee {
    private var available: Array<Coordinate> = Array(0){ Coordinate(0,0) }

    private val binding: ActivityGameForLocalBinding by lazy {
        ActivityGameForLocalBinding.inflate(layoutInflater)
    }

    private lateinit var board: Board

    private val TAG by lazy {
        applicationContext.getString(R.string.Dirtfy_test_tag)
    }

    private val resourceList = arrayOf(R.drawable.baseline_lens_24_red, R.drawable.baseline_lens_24_blue)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.gameBoardView.walls[WallType.Vertical.ordinal][0][0].post {
            val vertWalls = binding.gameBoardView.walls[WallType.Vertical.ordinal]
            val short = vertWalls[0][0].width
            val long = vertWalls[0][0].height

            binding.lowerPlayerWallSelector.verticalWallView.setSize(short, long)
            binding.lowerPlayerWallSelector.horizontalWallView.setSize(long, short)
            binding.upperPlayerWallSelector.verticalWallView.setSize(short, long)
            binding.upperPlayerWallSelector.horizontalWallView.setSize(long, short)
        }

        binding.gameBoardView.setWallChooseView(
            binding.lowerPlayerWallSelector.verticalWallChooseView,
            binding.lowerPlayerWallSelector.horizontalWallChooseView)
        binding.gameBoardView.setDragListener(object: GameBoardViewDropListener {
            override fun drop(matchedView: View, wallType: WallType, row: Int, col: Int): DropReturnType {
                val notationType = when(wallType) {
                    WallType.Vertical -> NotationType.VERTICAL
                    WallType.Horizontal -> NotationType.HORIZONTAL
                }
                val notation = Notation(notationType, Coordinate(row, col))

                return if (GameFunc.wallCross(notation, board) || GameFunc.wallClosed(notation, board)){
                    Toast.makeText(applicationContext, R.string.Cross_Wall, Toast.LENGTH_SHORT).show()
                    if(board.walls[wallType.ordinal][row][col]) DropReturnType.Match
                    else DropReturnType.Cross
                }
                else {
                    turnPass(notation)
                    DropReturnType.None
                }
            }
        })
        binding.gameBoardView.setClickListener(object: GameBoardViewPieceClickListener {
            override fun click(clickedPiece: View, row: Int, col: Int) {
                val cor = Coordinate(row, col)

                if(cor in available){
                    turnPass(Notation(NotationType.MOVE, cor))
//                    val p = board.nowPlayer()
//                    val champion = checkEnd(row,col)
//                    binding.gameBoardView.pieces[p.row][p.col].removeView(p.imageView)
//                    makePlayerShadow(Notation(NotationType.PLAYER, row, col))
//                    setWallChooseView(board.turn)
//                    binding.gameBoardView.pieces[row][col].addView(p.imageView)
//                    if(champion != -1){
//                        Toast.makeText(applicationContext,"${champion} Win!", Toast.LENGTH_LONG).show()
//                    }
                }
                else{
                    Toast.makeText(applicationContext,R.string.Cannot_Go, Toast.LENGTH_LONG).show()
                }
            }
        })

        initGame()
    }
//    private fun checkEnd(row:Int,col:Int): Int {
//        var victory = -1
//        when(board.turn){
//            0 ->{
//                if(row == 0) victory = 0
//            }
//            1 -> {
//                if(row == 8) victory = 1
//            }
//            2 -> {
//                if(col == 8) victory = 2
//            }
//            else -> {
//                if(col == 0) victory = 3
//            }
//        }
//        return victory
//    }

    private fun removePlayerShadow() {
        for(a in available){ //view 지우기
            binding.gameBoardView.pieces.get(a).removeAllViews()
        }
    }
    private fun addPlayerShadow(){
        available = GameFunc.getAvailableMove(turn, board)

        for(a in available){
            val playerShadow = createPlayerImageView(resourceList[turn])
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
    private fun removePlayer() {
        val cor = board.playerCorList[turn]
        binding.gameBoardView.pieces.get(cor).removeAllViews()
    }
    private fun addPlayer() {
        val cor = board.playerCorList[turn]
        val playerImage = createPlayerImageView(resourceList[turn])
        binding.gameBoardView.pieces.get(cor).addView(playerImage)
    }

    var nowTurn = 0
    lateinit var players: Array<com.example.quoridor.game.Player>

    override var turn: Int
        get() = nowTurn
        set(value) {nowTurn = value}
    override val playerList: Array<com.example.quoridor.game.Player>
        get() = players

    private fun setPlayerInfoView(player: LocalPlayer, playerInfo: MyPlayInfoView) {
        playerInfo.leftTimeTextView.text = millToMinSec(player.timer.left)
        val wallText = getString(R.string.left_wall_prefix)+player.leftWall.toString()
        playerInfo.leftWallTextView.text = wallText
    }

    override fun initGame() {
        val leftWall = 10
        val timeLimit: Long = 60 * 1000

        val player0 = LocalPlayer(
            createPlayerImageView(resourceList[0]),
            TimeCounter(
                this,
                binding.lowerPlayerInfoView.leftTimeTextView,
                timeLimit,
                object : TimeCounterOverListener {
                    override fun timeOver() {
                        Toast.makeText(applicationContext, "p0 time over", Toast.LENGTH_SHORT).show()
                    }
                }),
            Coordinate(8, 4),
            leftWall
        )
        val lowerPlayerInfo = binding.lowerPlayerInfoView
        setPlayerInfoView(player0, lowerPlayerInfo)

        val player1 = LocalPlayer(
            createPlayerImageView(resourceList[1]),
            TimeCounter(
                this,
                binding.upperPlayerInfoView.leftTimeTextView,
                timeLimit,
                object : TimeCounterOverListener {
                    override fun timeOver() {
                      Toast.makeText(applicationContext, "p1 time over", Toast.LENGTH_SHORT).show()
                    }
                }),
            Coordinate(0, 4),
            leftWall
        )
        val upperPlayerInfo = binding.upperPlayerInfoView
        setPlayerInfoView(player1, upperPlayerInfo)

        players = arrayOf(player0, player1)
        board = Board(
            arrayOf(player0.coordinate, player1.coordinate),
            Array(2){ Array(8) { Array(8) { false }}}
        )

        binding.gameBoardView.pieces.get(player0.coordinate).addView(player0.image)
        binding.gameBoardView.pieces.get(player1.coordinate).addView(player1.image)

        setWallChooseView(binding.lowerPlayerWallSelector)
        addPlayerShadow()
        player0.turnStart()
    }

    override fun turnPass(notation: Notation) {
        players[turn].turnEnd()

        removePlayerShadow()

        if (notation.type == NotationType.MOVE) {
            val playerImage = (players[turn] as LocalPlayer).image
            binding.gameBoardView.pieces.get(board.playerCorList[turn]).removeView(playerImage)
            binding.gameBoardView.pieces.get(notation.coordinate).addView(playerImage)
        }

        board.doNotation(notation, this)
        setPlayerInfoView(players[turn] as LocalPlayer,
            when(turn) {
                0 -> binding.lowerPlayerInfoView
                else ->  binding.upperPlayerInfoView
            })

        turn = (turn+1)%2

        setWallChooseView(when(turn){
            0 -> binding.lowerPlayerWallSelector
            else -> binding.upperPlayerWallSelector
        })
        addPlayerShadow()

        players[turn].turnStart()
    }
}