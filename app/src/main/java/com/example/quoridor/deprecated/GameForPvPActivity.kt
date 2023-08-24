//package com.example.quoridor
//
//import android.app.Dialog
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import android.os.CountDownTimer
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import android.view.Window
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.quoridor.communication.retrofit.HttpDTO
//import com.example.quoridor.communication.socket.WebSocketDTO
//import com.example.quoridor.communication.socket.WebSocketTest
//import com.example.quoridor.customView.gameBoardView.Board
//import com.example.quoridor.customView.gameBoardView.GameBoardViewPieceClickListener
//import com.example.quoridor.customView.gameBoardView.GameBoardViewPlayerImageGetter
//import com.example.quoridor.customView.gameBoardView.GameBoardViewWallListener
//import com.example.quoridor.customView.gameBoardView.Wall
//import com.example.quoridor.customView.gameBoardView.WallType
//import com.example.quoridor.customView.playerView.Player
//import com.example.quoridor.databinding.ActivityGameForPvpBinding
//import com.example.quoridor.databinding.DialogGameEndBinding
//import com.example.quoridor.game.Notation
//import com.example.quoridor.game.types.ActionType
//import com.example.quoridor.game.types.GameResultType
//import com.example.quoridor.game.types.GameType
//import com.example.quoridor.game.types.NotationType
//import com.example.quoridor.game.util.GameFunc
//import com.example.quoridor.game.util.GameFunc.getGameType
//import com.example.quoridor.game.util.GameFunc.getMatchData
//import com.example.quoridor.util.Coordinate
//import com.example.quoridor.util.Func.get
//import com.example.quoridor.util.Func.popToast
//import com.example.quoridor.util.Func.set
//import com.example.quoridor.util.Func.setSize
//import com.google.gson.Gson
//import okhttp3.JavaNetCookieJar
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.Response
//import okhttp3.WebSocket
//import okhttp3.WebSocketListener
//import okio.ByteString
//import java.net.CookieManager
//import java.util.concurrent.TimeUnit
//
//class GameForPvPActivity:  AppCompatActivity() {
//
//    private val client = OkHttpClient.Builder()
//        .connectTimeout(10, TimeUnit.SECONDS)
//        .callTimeout(30, TimeUnit.SECONDS)
//        .readTimeout(30, TimeUnit.SECONDS)
//        .writeTimeout(30, TimeUnit.SECONDS)
//        .pingInterval(30, TimeUnit.SECONDS)
//        .cookieJar(JavaNetCookieJar(CookieManager()))
//        .build()
//
//    companion object {
//        lateinit var URL: String
//
//        lateinit var request: Request
//        lateinit var listener: WebSocketListener
//
//        lateinit var ws: WebSocket
//
//        val gson = Gson()
//    }
//
//    private val binding: ActivityGameForPvpBinding by lazy {
//        ActivityGameForPvpBinding.inflate(layoutInflater)
//    }
//
//    private val playersData by lazy {
//        arrayOf(binding.myInfoView.data, binding.opPlayerInfoView.data)
//    }
//    private val boardData by lazy {
//        binding.gameBoardView.data
//    }
//    private var available: Array<Coordinate> = Array(0){ Coordinate(0,0) }
//
//    private lateinit var matchData: HttpDTO.MatchingResponse
//    private lateinit var gameType: GameType
//
//    private val imageResourceList = arrayOf( R.drawable.baseline_lens_24_red, R.drawable.baseline_lens_24_blue)
//    private val imageViewList by lazy {
//        Array(2) { ImageView(applicationContext) }
//    }
//
//    private var timer: CountDownTimer? = null
//    private var turn = 0
//
//    private val TAG by lazy {
//        applicationContext.getString(R.string.Dirtfy_test_tag)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        gameType = intent.getGameType()
//        matchData = intent.getMatchData()
//
//        URL = WebSocketTest.BASE_URL +"game/move?"+"gameId="+ matchData.gameId+"&turn="+ matchData.turn.toString()
//        request = Request.Builder().url(URL).build()
//        listener = object : WebSocketListener() {
//            override fun onOpen(webSocket: WebSocket, response: Response) {
//                super.onOpen(webSocket, response)
//                Log.d(TAG, "web socket opened")
//            }
//
//            override fun onMessage(webSocket: WebSocket, text: String) {
//                super.onMessage(webSocket, text)
//
//                val action = gson.fromJson(text, WebSocketDTO.Action::class.java)
//
//                when (action.type) {
//                    ActionType.VERTICAL.ordinal -> {
//                        turnPass(Notation(NotationType.VERTICAL, Coordinate(action.row, action.col)))
//                    }
//                    ActionType.HORIZONTAL.ordinal -> {
//                        turnPass(Notation(NotationType.HORIZONTAL, Coordinate(action.row, action.col)))
//                    }
//                    ActionType.MOVE.ordinal -> {
//                        turnPass(Notation(NotationType.MOVE, Coordinate(action.row, action.col)))
//                    }
//                    ActionType.WIN.ordinal -> {
//                        gameEnd((matchData.turn?.plus(1))?.rem(2) ?: turn)
//                    }
//                    ActionType.LOSE.ordinal -> {
//                        gameEnd(matchData.turn?: turn)
//                    }
//                }
//            }
//
//            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//                super.onMessage(webSocket, bytes)
//            }
//
//            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
//                super.onClosing(webSocket, code, reason)
//            }
//
//            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
//                super.onClosed(webSocket, code, reason)
//            }
//
//            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//                super.onFailure(webSocket, t, response)
//            }
//
//        }
//        ws = client.newWebSocket(request, listener)
//
//        binding.gameBoardView.walls[WallType.VERTICAL.ordinal][0][0].post {
//            val vertWalls = binding.gameBoardView.walls[WallType.VERTICAL.ordinal]
//            val short = vertWalls[0][0].width
//            val long = vertWalls[0][0].height
//
//            binding.myWallSelector.verticalWallView.setSize(short, long)
//            binding.myWallSelector.horizontalWallView.setSize(long, short)
//        }
//
//        if (matchData.turn != 0) {
//            binding.gameBoardView.apply {
//                rotation = 180F
//            }
//        }
//
//        binding.gameBoardView.setDropListener(object : GameBoardViewWallListener.DropListener {
//            override fun cross(matchedView: View, wall: Wall): Boolean {
//                if (GameFunc.wallCross(wall, boardData.value!!)) {
//                    popToast(this@GameForPvPActivity, "cross")
//                    return true
//                }
//                return false
//            }
//
//            override fun closed(matchedView: View, wall: Wall): Boolean {
//                if (GameFunc.wallClosed(wall, boardData.value!!)) {
//                    popToast(this@GameForPvPActivity, "closed")
//                    return true
//                }
//                return false
//            }
//
//            override fun match(matchedView: View, wall: Wall): Boolean {
//                if (GameFunc.wallMatch(wall, boardData.value!!)) {
//                    popToast(this@GameForPvPActivity, "match")
//                    return true
//                }
//                return false
//            }
//
//            override fun success(matchedView: View, wall: Wall): Boolean {
//                return if (turn == matchData.turn) false
//                else {
//                    val notationType = when(wall.type) {
//                        WallType.VERTICAL -> NotationType.VERTICAL
//                        WallType.HORIZONTAL -> NotationType.HORIZONTAL
//                    }
//                    val notation = Notation(notationType, wall.coordinate)
//                    turnPass(notation)
//
//                    true
//                }
//
//            }
//        })
//        binding.gameBoardView.setClickListener(object: GameBoardViewPieceClickListener {
//            override fun click(clickedPiece: View, row: Int, col: Int) {
//                val cor = Coordinate(row, col)
//
//                if(cor in available && turn == matchData.turn){
//                    turnPass(Notation(NotationType.MOVE, cor))
//                }
//                else{
//                    Toast.makeText(applicationContext,R.string.Cannot_Go, Toast.LENGTH_LONG).show()
//                }
//            }
//        })
//        binding.gameBoardView.setImageGetter(object : GameBoardViewPlayerImageGetter {
//            override fun getImageView(playerNum: Int): ImageView {
//                return imageViewList[playerNum]
//            }
//        })
//
//        initGame()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        timer?.cancel()
//        ws.send("${WebSocketDTO.Action(playersData[matchData.turn?: 0].value?.leftTime?: 0, GameResultType.LOSE.ordinal, -1, -1)}")
//        ws.close(1000, "onPause")
//    }
//
//    override fun onBackPressed() {
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.D_transparent)))
//        dialog.setContentView(R.layout.dialog_game_quit)
//
//        val yesBtn = dialog.findViewById<Button>(R.id.yes_btn)
//        val noBtn = dialog.findViewById<Button>(R.id.no_btn)
//
//        yesBtn.setOnClickListener {
//            dialog.dismiss()
//            this@GameForPvPActivity.finish()
//        }
//        noBtn.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }
//
//    private fun gameEnd(winner: Int) {
//        val looser = (winner+1)%2
//
//        timer?.cancel()
//
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.D_transparent)))
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.dialog_game_end)
//
//        val dialogBinding = DialogGameEndBinding.bind(dialog.findViewById(R.id.top_layout))
//
//        val winnerData = playersData[winner].value!!
//        val looserData = playersData[looser].value!!
//
//        dialogBinding.winnerNameTv.text = winnerData.name
//        dialogBinding.winnerProfileIv.setImageResource(imageResourceList[winner])
//
//        val player0 = playersData[0].value!!
//        dialogBinding.p0NameTv.text = player0.name
//        dialogBinding.p0BeforeRating.text = player0.rating.toString()
//
//        val player1 = playersData[1].value!!
//        dialogBinding.p1NameTv.text = player1.name
//        dialogBinding.p1BeforeRating.text = player1.rating.toString()
//
//        val winnerAfterRating = GameFunc.calcRating(winnerData.rating, looserData.rating, GameResultType.WIN)
//        val looserAfterRating = GameFunc.calcRating(looserData.rating, winnerData.rating, GameResultType.LOSE)
//
//        if (winner == 0) {
//            dialogBinding.p0AfterRating.text = winnerAfterRating.toString()
//            dialogBinding.p1AfterRating.text = looserAfterRating.toString()
//        }
//        else {
//            dialogBinding.p1AfterRating.text = winnerAfterRating.toString()
//            dialogBinding.p0AfterRating.text = looserAfterRating.toString()
//        }
//
//        dialogBinding.okBtn.setOnClickListener {
//            dialog.dismiss()
//            this@GameForPvPActivity.finish()
//        }
//
//        dialog.show()
//    }
//
//    private fun removePlayerShadow() {
//        for(a in available){ //view 지우기
//            binding.gameBoardView.pieces.get(a).removeAllViews()
//        }
//    }
//    private fun addPlayerShadow(playerNum: Int){
//        if (boardData.value == null)
//            return
//
//        val boardValue = boardData.value!!
//        available = GameFunc.getAvailableMove(playerNum, boardValue)
//
//        for(a in available){
//            val playerShadow = createPlayerImageView(imageResourceList[playerNum])
//            playerShadow.alpha = 0.5f
//            binding.gameBoardView.pieces.get(a).addView(playerShadow)
//        }
//    }
//    private fun createPlayerImageView(imageId: Int): ImageView {
//        val image = ImageView(applicationContext)
//        image.setImageResource(imageId)
//        image.adjustViewBounds = true
//        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        image.layoutParams = lp
//        return image
//    }
//
//    private fun buildTimer(playerNum: Int, timeOver: () -> Unit = {}): CountDownTimer? {
//        if (playersData[playerNum].value == null)
//            return null
//
//        val playerValue = playersData[playerNum].value!!
//        return object : CountDownTimer(playerValue.leftTime, 1000L) {
//            override fun onTick(p0: Long) {
//                playerValue.leftTime -= 1000L
//                playersData[playerNum].postValue(playerValue)
//                Log.d(TAG, "timer is running ${playerValue.leftTime}")
//            }
//
//            override fun onFinish() {
//                timeOver()
//            }
//        }.start()
//    }
//
//    fun initGame() {
//        val player0 = Player("p0", gameType.timeLimit, gameType.initWall, 1050)
//        binding.myInfoView.profileImageView.setImageResource(R.drawable.hobanwoo_red)
//        binding.myInfoView.data.value = player0
//        val player1 = Player("p1", gameType.timeLimit, gameType.initWall, 950)
//        binding.opPlayerInfoView.profileImageView.setImageResource(R.drawable.hobanwoo_blue)
//        binding.opPlayerInfoView.data.value = player1
//
//        val board = Board()
//        binding.gameBoardView.data.value = board
//
//        imageViewList[0] = createPlayerImageView(imageResourceList[0])
//        imageViewList[1] = createPlayerImageView(imageResourceList[1])
//
//        if (matchData.turn == 0) {
//            binding.gameBoardView.setWallChooseView(
//                binding.myWallSelector.verticalWallChooseView,
//                binding.myWallSelector.horizontalWallChooseView
//            )
//        }
//
//        addPlayerShadow(0)
//
//        timer = buildTimer(0) {
//            gameEnd(1)
//            if (turn == matchData.turn)
//                ws.send("${WebSocketDTO.Action(0L, GameResultType.LOSE.ordinal, 0, 0)}")
//        }
//    }
//
//    fun turnPass(notation: Notation) {
//        timer?.cancel()
//
//        binding.gameBoardView.setWallChooseView(
//            View(applicationContext),
//            View(applicationContext)
//        )
//
//        if (turn == matchData.turn) {
//            removePlayerShadow()
//        }
//
//        val playerValue = playersData[turn].value!!
//        val boardValue = boardData.value!!
//        when(notation.type) {
//            NotationType.MOVE -> {
//                boardValue.playCoordinates[turn] = notation.coordinate
//            }
//            NotationType.VERTICAL -> {
//                boardValue.verticalWalls.set(notation.coordinate, true)
//                playerValue.leftWall -= 1
//            }
//            NotationType.HORIZONTAL -> {
//                boardValue.horizontalWalls.set(notation.coordinate, true)
//                playerValue.leftWall -= 1
//            }
//        }
//        playersData[turn].value = playerValue
//        boardData.value = boardValue
//
//        turn = (turn+1)%2
//
//        if (turn == matchData.turn) {
//            addPlayerShadow(turn)
//            binding.gameBoardView.setWallChooseView(
//                binding.myWallSelector.verticalWallChooseView,
//                binding.myWallSelector.verticalWallChooseView
//            )
//        }
//
//        timer = buildTimer(turn) {
//            gameEnd((turn+1)%2)
//            if (turn == matchData.turn)
//                ws.send("${WebSocketDTO.Action(0L, GameResultType.LOSE.ordinal, 0, 0)}")
//        }
//    }
//
//}