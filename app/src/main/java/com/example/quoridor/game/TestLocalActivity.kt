package com.example.quoridor.game

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.quoridor.R
import com.example.quoridor.customView.WallSelectorView
import com.example.quoridor.customView.gameBoardView.Board
import com.example.quoridor.customView.gameBoardView.GameBoardView
import com.example.quoridor.customView.gameBoardView.GameBoardViewPieceClickListener
import com.example.quoridor.customView.gameBoardView.GameBoardViewWallListener
import com.example.quoridor.customView.gameBoardView.Wall
import com.example.quoridor.customView.gameBoardView.WallType
import com.example.quoridor.customView.playerView.Player
import com.example.quoridor.databinding.ActivityGameForLocalBinding
import com.example.quoridor.util.Coordinate
import com.example.quoridor.util.Func
import com.example.quoridor.util.Func.setSize

class TestLocalActivity: GameActivity() {

    private val binding by lazy {
        ActivityGameForLocalBinding.inflate(layoutInflater)
    }
    private val ivList by lazy {
        Array(2) { ImageView(applicationContext) }
    }

    override val gameBoardView: GameBoardView
        get() = binding.gameBoardView
    override val dragListener: GameBoardViewWallListener.DragListener
        get() = object : GameBoardViewWallListener.DragListener {
            override fun startDrag(): Boolean {
                return if (!viewModel.isWallLeft()) {
                    Func.popToast(this@TestLocalActivity, "no left wall")
                    false
                } else true
            }
        }
    override val dropListener: GameBoardViewWallListener.DropListener
        get() = object : GameBoardViewWallListener.DropListener {
            override fun cross(matchedView: View, wall: Wall): Boolean {
                if (viewModel.wallCross(wall)) {
                    Func.popToast(this@TestLocalActivity, "cross")
                    return true
                }
                return false
            }

            override fun closed(matchedView: View, wall: Wall): Boolean {
                if (viewModel.wallClosed(wall)) {
                    Func.popToast(this@TestLocalActivity, "closed")
                    return true
                }
                return false
            }

            override fun match(matchedView: View, wall: Wall): Boolean {
                if (viewModel.wallMatch(wall)) {
                    Func.popToast(this@TestLocalActivity, "match")
                    return true
                }
                return false
            }

            override fun success(matchedView: View, wall: Wall) {
                when(wall.type) {
                    WallType.VERTICAL -> {
                        viewModel.addVerticalWall(wall.coordinate)
                    }
                    WallType.HORIZONTAL -> {
                        viewModel.addHorizontalWall(wall.coordinate)
                    }
                }
                viewModel.turnPass()
            }
        }
    override val clickListener: GameBoardViewPieceClickListener
        get() = object: GameBoardViewPieceClickListener {
            override fun click(clickedPiece: View, coordinate: Coordinate) {
                if(viewModel.isAvailableMove(coordinate)){
                    viewModel.move(coordinate)
                    viewModel.setAvailableMove(arrayOf())
                    if (!viewModel.isEnd.value!!)
                        viewModel.turnPass()
                }
                else{
                    Toast.makeText(applicationContext, R.string.Cannot_Go, Toast.LENGTH_LONG).show()
                }
            }
        }
    override val imageViewList: Array<ImageView>
        get() = ivList
    override val TAG: String
        get() = "$_TAG - TestLocalActivity"

    override fun initGame() {
        val player0 = Player("p0", gameType.timeLimit, gameType.initWall, 1050, true)
        binding.lowerPlayerInfoView.data.value = player0
        val player1 = Player("p1", gameType.timeLimit, gameType.initWall, 950, false)
        binding.upperPlayerInfoView.data.value = player1

        val board = Board()
        binding.gameBoardView.data.value = board

        viewModel.board.value = board
        viewModel.players[0].value = player0
        viewModel.players[1].value = player1
        viewModel.availableMoves.value = arrayOf()
        viewModel.turn.value = 0
        viewModel.isEnd.value = false

        ivList[0] = createPlayerImageView(imageResourceList[0])
        ivList[1] = createPlayerImageView(imageResourceList[1])

        setWallChooseView(binding.lowerPlayerWallSelector)
        viewModel.getAvailableMoves(0)

        timer = buildTimer(0) {
            gameEnd(1)
        }
    }

    override fun setWallSize() {
        binding.gameBoardView.walls[WallType.VERTICAL.ordinal][0][0].post {
            val vertWalls = binding.gameBoardView.walls[WallType.VERTICAL.ordinal]
            val short = vertWalls[0][0].width
            val long = vertWalls[0][0].height

            binding.lowerPlayerWallSelector.verticalWallView.setSize(short, long)
            binding.lowerPlayerWallSelector.horizontalWallView.setSize(long, short)
            binding.upperPlayerWallSelector.verticalWallView.setSize(short, long)
            binding.upperPlayerWallSelector.horizontalWallView.setSize(long, short)
        }
    }

    override fun player0Observe(player: Player) {
        binding.lowerPlayerInfoView.data.value = player
    }

    override fun player1Observe(player: Player) {
        binding.upperPlayerInfoView.data.value = player
    }

    override fun turnObserve(turn: Int) {
        Log.d(TAG, "$turn")

        timer?.cancel()

        setWallChooseView(when(turn){
            0 -> binding.lowerPlayerWallSelector
            else -> binding.upperPlayerWallSelector
        })

        viewModel.getAvailableMoves()

        timer = buildTimer(turn) {
            gameEnd((turn+1)%2)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun setWallChooseView(selector: WallSelectorView) {
        binding.gameBoardView.setWallChooseView(
            selector.verticalWallChooseView,
            selector.horizontalWallChooseView)
    }
}