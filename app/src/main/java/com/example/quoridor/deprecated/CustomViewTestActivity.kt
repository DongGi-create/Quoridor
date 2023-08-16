//package com.example.quoridor.deprecated
//
//import android.os.Bundle
//import android.util.Log
//import android.util.Pair
//import android.view.RoundedCorner
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import com.example.quoridor.R
//import com.example.quoridor.databinding.ActivityCustomViewTestBinding
//import com.example.quoridor.domain.Notation
//import com.example.quoridor.domain.Player
//import com.example.quoridor.domain.utils.NotationType
//import com.example.quoridor.domain.utils.PlayerColorType
//import com.example.quoridor.customView.gameBoardView.GameBoardViewDropListener
//import com.example.quoridor.customView.gameBoardView.GameBoardViewPieceClickListener
//import com.example.quoridor.game.frontDomain.FrontBoard
//import com.example.quoridor.game.frontDomain.FrontPlayer
//import com.example.quoridor.util.Coordinate
//import com.example.quoridor.game.util.GameFunc
//import com.example.quoridor.game.util.timeCounter.TimeCounter
//import com.example.quoridor.game.util.timeCounter.TimeCounterOverListener
//import com.example.quoridor.game.util.types.DropReturnType
//import com.example.quoridor.game.util.types.WallType
//
//class CustomViewTestActivity : ComponentActivity() {
//    private var available: Array<Coordinate> = Array(0){ Coordinate(0,0) }
//
//    private val binding: ActivityCustomViewTestBinding by lazy {
//        ActivityCustomViewTestBinding.inflate(layoutInflater)
//    }
//
//    private lateinit var board: FrontBoard
//
//    private val TAG by lazy {
//        applicationContext.getString(R.string.Dirtfy_test_tag)
//    }
//
//    private val resourceList = arrayOf(R.drawable.hobanwoo_red, R.drawable.hobanwoo_blue,R.drawable.hobanwoo_green,R.drawable.hobanwoo_black)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        binding.gameBoardView.walls[WallType.Vertical.ordinal][0][0].post {
//            val vertWalls = binding.gameBoardView.walls[WallType.Vertical.ordinal]
//            val short = vertWalls[0][0].width
//            val long = vertWalls[0][0].height
//
//            var lp = binding.vertWall.layoutParams
//            lp.width = short
//            lp.height = long
//            binding.vertWall.layoutParams = lp
//            Log.d(TAG, "vw w:${binding.vertWall.width}, h:${binding.vertWall.height}")
//
//            lp = binding.horiWall.layoutParams
//            lp.width = long
//            lp.height = short
//            binding.horiWall.layoutParams = lp
//        }
//
//        binding.gameBoardView.setWallChooseView(binding.vertWallLayout, binding.horiWallLayout)
//        binding.gameBoardView.setDragListener(object: GameBoardViewDropListener {
//            override fun drop(matchedView: View, wallType: WallType, row: Int, col: Int): DropReturnType {
//                val notationType = when(wallType) {
//                    WallType.Vertical -> NotationType.VERTICAL
//                    WallType.Horizontal -> NotationType.HORIZONTAL
//                }
//                val notation = Notation(notationType, row, col)
//
//                return if (GameFunc.wallCross(notation, board) || GameFunc.wallClosed(notation, board)){
//                    Toast.makeText(applicationContext, R.string.Cross_Wall, Toast.LENGTH_SHORT).show()
//                    if(board.walls[wallType.ordinal][row][col]) DropReturnType.Match
//                    else DropReturnType.Cross
//                }
//                else {
//                    makePlayerShadow(notation)
//                    DropReturnType.None
//                }
//            }
//        })
//        binding.gameBoardView.setClickListener(object: GameBoardViewPieceClickListener {
//            override fun click(clickedPiece: View, row: Int, col: Int) {
//                var CanGo = false
//                for(a in available) {
//                    if (Coordinate(row, col) == a){
//                        CanGo = true
//                        break
//                    }
//                }
//                if(CanGo){
//                    val p = board.nowPlayer()
//                    var champion = checkEnd(row,col)
//                    binding.gameBoardView.pieces[p.row][p.col].removeView(p.imageView)
//                    makePlayerShadow(Notation(NotationType.PLAYER, row, col))
//                    binding.gameBoardView.pieces[row][col].addView(p.imageView)
//                    if(champion != -1){
//                        Toast.makeText(applicationContext,"${champion} Win!",Toast.LENGTH_LONG).show()
//                    }
//                }
//                else{
//                    Toast.makeText(applicationContext,R.string.Cannot_Go,Toast.LENGTH_LONG).show()
//                }
//            }
//        })
//
//        setGame()
//        board.start()
//        available = GameFunc.getAvailableMove(board.turn,board)
//        for(a in available){ //가능한 곳 색칠
//            val playerShadow = createPlayerImageView(resourceList[0])
//            playerShadow.alpha = 0.5f
//            binding.gameBoardView.pieces[a.r][a.c].addView(playerShadow)
//        }
//    }
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
//
//    private fun makePlayerShadow(notation : Notation){
//        for(a in available){ //view 지우기
//            binding.gameBoardView.pieces[a.r][a.c].removeAllViews()
//        }
//        //val notation = Notation(NotationType.PLAYER, row, col)
//        board.doNotation(notation)
//        available = GameFunc.getAvailableMove(board.turn, board)
//        for(a in available){
//            val playerShadow = createPlayerImageView(resourceList[board.turn])
//            playerShadow.alpha = 0.5f
//            binding.gameBoardView.pieces[a.r][a.c].addView(playerShadow)
//        }
//    }
//    private fun createPlayerImageView(imageId: Int): ImageView{
//        val image = ImageView(applicationContext)
//        image.setImageResource(imageId)
//        image.adjustViewBounds = true
//        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        image.layoutParams = lp
//        return image
//    }
//
//    private fun setGame(){
//        val leftWall = 10
//        val timeLimit: Long = 60 * 1000
//
//        val player0 = FrontPlayer(
//            createPlayerImageView(R.drawable.hobanwoo_red),
//            TimeCounter(this, binding.myInfo.leftTimeTextView, timeLimit, object : TimeCounterOverListener {
//                override fun timeOver() {
//                    Toast.makeText(applicationContext, "p0 time over", Toast.LENGTH_SHORT).show()
//                }
//            }),
//            Player(leftWall, timeLimit, 8, 4, "p0", PlayerColorType.RED)
//        )
//        binding.myInfo.setPlayer(player0)
//
//        val player1 = FrontPlayer(
//            createPlayerImageView(R.drawable.hobanwoo_blue),
//            TimeCounter(this, binding.player1Info.leftTimeTextView, timeLimit, object : TimeCounterOverListener {
//                override fun timeOver() {
//                    Toast.makeText(applicationContext, "p1 time over", Toast.LENGTH_SHORT).show()
//                }
//            }),
//            Player(leftWall, timeLimit, 0, 4, "p1", PlayerColorType.BLUE)
//        )
//        binding.player1Info.setPlayer(player1)
//
//        val players = arrayOf(player0, player1)
//        val verticalWalls = ArrayList<Pair<Int, Int>>()
//        val horizontalWalls = ArrayList<Pair<Int, Int>>()
//
//        board = FrontBoard(players, verticalWalls, horizontalWalls, true)
//
//        binding.gameBoardView.setBoard(board)
//    }
//}