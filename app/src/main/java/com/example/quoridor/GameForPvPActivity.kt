package com.example.quoridor

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.socket.WebSocketDTO
import com.example.quoridor.communication.socket.WebSocketTest
import com.example.quoridor.customView.gameBoardView.Board
import com.example.quoridor.customView.gameBoardView.GameBoardView
import com.example.quoridor.customView.gameBoardView.GameBoardViewPieceClickListener
import com.example.quoridor.customView.gameBoardView.GameBoardViewWallListener
import com.example.quoridor.customView.gameBoardView.Wall
import com.example.quoridor.customView.gameBoardView.WallType
import com.example.quoridor.customView.playerView.Player
import com.example.quoridor.databinding.ActivityGameForPvpBinding
import com.example.quoridor.game.GameActivity
import com.example.quoridor.game.types.ActionType
import com.example.quoridor.game.util.GameFunc.getMatchData
import com.example.quoridor.login.UserManager
import com.example.quoridor.util.Coordinate
import com.example.quoridor.util.Func
import com.example.quoridor.util.Func.popToast
import com.example.quoridor.util.Func.setSize
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.net.CookieManager
import java.util.concurrent.TimeUnit

class GameForPvPActivity: GameActivity() {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .pingInterval(30, TimeUnit.SECONDS)
        .cookieJar(JavaNetCookieJar(CookieManager()))
        .build()
    private lateinit var URL: String

    private lateinit var request: Request
    private lateinit var listener: WebSocketListener

    private lateinit var ws: WebSocket

    private val gson = Gson()

    private val binding by lazy {
        ActivityGameForPvpBinding.inflate(layoutInflater)
    }
    private val ivList by lazy {
        Array(2) { ImageView(applicationContext) }
    }

    private lateinit var matchData: HttpDTO.Response.Match
    private val myTurn by lazy {
        matchData.turn!!
    }
    private val opTurn by lazy {
        (matchData.turn!!+1)%2
    }

    override val gameBoardView: GameBoardView
        get() = binding.gameBoardView
    override val imageViewList: Array<ImageView>
        get() = ivList
    override val dragListener: GameBoardViewWallListener.DragListener
        get() = object :  GameBoardViewWallListener.DragListener{
            override fun startDrag(): Boolean {
                return if (viewModel.isMyTurn(myTurn)){
                    popToast(this@GameForPvPActivity, "not my turn")
                    false
                }
                else if (viewModel.isWallLeft(myTurn)) {
                    popToast(this@GameForPvPActivity, "no left wall")
                    false
                }
                else true
            }
        }
    override val dropListener: GameBoardViewWallListener.DropListener
        get() = object : GameBoardViewWallListener.DropListener {
            override fun cross(matchedView: View, wall: Wall): Boolean {
                if (viewModel.wallCross(wall)) {
                    Func.popToast(this@GameForPvPActivity, "cross")
                    return true
                }
                return false
            }

            override fun closed(matchedView: View, wall: Wall): Boolean {
                if (viewModel.wallClosed(wall)) {
                    Func.popToast(this@GameForPvPActivity, "closed")
                    return true
                }
                return false
            }

            override fun match(matchedView: View, wall: Wall): Boolean {
                if (viewModel.wallMatch(wall)) {
                    Func.popToast(this@GameForPvPActivity, "match")
                    return true
                }
                return false
            }

            override fun success(matchedView: View, wall: Wall) {
                when(wall.type) {
                    WallType.VERTICAL -> {
                        viewModel.addVerticalWall(wall.coordinate)
                        send(makeMessage(ActionType.VERTICAL, wall.coordinate))
                    }
                    WallType.HORIZONTAL -> {
                        viewModel.addHorizontalWall(wall.coordinate)
                        send(makeMessage(ActionType.HORIZONTAL, wall.coordinate))
                    }
                }
                viewModel.turnPass()
            }
        }
    override val clickListener: GameBoardViewPieceClickListener
        get() = object: GameBoardViewPieceClickListener {
            override fun click(clickedPiece: View, coordinate: Coordinate) {
                if(viewModel.isAvailableMove(coordinate) && viewModel.isMyTurn(myTurn)){
                    viewModel.move(coordinate)
                    viewModel.setAvailableMove(arrayOf())
                    send(makeMessage(ActionType.MOVE, coordinate))
                    if (!viewModel.isEnd.value!!)
                        viewModel.turnPass()
                }
                else{
                    popToast(this@GameForPvPActivity, "unavailable")
                }
            }
        }

    override val TAG: String
        get() = "$_TAG - GameForPvpActivity"

    override fun onPause() {
        super.onPause()
        send(makeMessage())
        ws.close(1000, "onPause")
    }

    override fun initGame() {

        if (myTurn != 0) {
            gameBoardView.apply {
                rotation = 180F
            }
        }

        val player0 = Player("p0", gameType.timeLimit, gameType.initWall, 1050, true)
        binding.myInfoView.profileImageView.setImageResource(R.drawable.hobanwoo_red)
//        binding.myInfoView.data.value = player0
        binding.myInfoView.data.postValue(player0)
        val player1 = Player("p1", gameType.timeLimit, gameType.initWall, 950, false)
        binding.opPlayerInfoView.profileImageView.setImageResource(R.drawable.hobanwoo_blue)
//        binding.opPlayerInfoView.data.value = player1
        binding.opPlayerInfoView.data.postValue(player1)

        val board = Board()
//        gameBoardView.data.value = board
        gameBoardView.data.postValue(board)

//        viewModel.board.value = board
//        viewModel.players[0].value = player0
//        viewModel.players[1].value = player1
//        viewModel.availableMoves.value = arrayOf()
//        viewModel.turn.value = 0
//        viewModel.isEnd.value = false
        viewModel.board.postValue(board)
        viewModel.players[0].postValue(player0)
        viewModel.players[1].postValue(player1)
        viewModel.availableMoves.postValue(arrayOf())
        viewModel.turn.postValue(0)
        viewModel.isEnd.postValue(false)

        ivList[0] = createPlayerImageView(imageResourceList[0])
        ivList[1] = createPlayerImageView(imageResourceList[1])

        binding.gameBoardView.setWallChooseView(
            binding.myWallSelector.verticalWallChooseView,
            binding.myWallSelector.verticalWallChooseView
        )

        timer = buildTimer(0) {
            gameEnd(1)
            if (viewModel.isMyTurn(myTurn))
                send(makeMessage())
        }
    }

    override fun setWallSize() {
        gameBoardView.walls[WallType.VERTICAL.ordinal][0][0].post {
            val vertWalls = gameBoardView.walls[WallType.VERTICAL.ordinal]
            val short = vertWalls[0][0].width
            val long = vertWalls[0][0].height

            binding.myWallSelector.verticalWallView.setSize(short, long)
            binding.myWallSelector.horizontalWallView.setSize(long, short)
        }
    }

    override fun player0Observe(player: Player) {
        if (myTurn == 0)
            binding.myInfoView.data.postValue(player)
//            binding.myInfoView.data.value = player
        else
            binding.opPlayerInfoView.data.postValue(player)
//            binding.opPlayerInfoView.data.value = player
    }

    override fun player1Observe(player: Player) {
        if (myTurn == 1)
            binding.myInfoView.data.postValue(player)
//            binding.myInfoView.data.value = player
        else
            binding.opPlayerInfoView.data.postValue(player)
//            binding.opPlayerInfoView.data.value = player
    }

    override fun turnObserve(turn: Int) {
        timer?.cancel()

        if (turn == matchData.turn)
            viewModel.getAvailableMoves()
        else
            viewModel.setAvailableMove(arrayOf())

        timer = buildTimer(turn) {
            gameEnd((turn+1)%2)
            if (turn == myTurn)
                send(makeMessage())
        }
    }

    override fun gameEnd(winner: Int) {
        super.gameEnd(winner)
        val remainTime = viewModel.players[myTurn].value!!.leftTime
        val cor = viewModel.board.value!!.playCoordinates[myTurn]
        send(WebSocketDTO.Action(
            remainTime,
            ActionType.WIN.ordinal,
            cor.r,
            cor.c))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        matchData = intent.getMatchData()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        URL = "${WebSocketTest.BASE_URL}game/move?uid=${UserManager.umuid}&gameId=${matchData.gameId}&turn=${matchData.turn}"
        request = Request.Builder().url(URL).build()
        listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d(TAG, "web socket opened\n$response")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d(TAG, "web socket messaged\n$text")

                val action = gson.fromJson(text, WebSocketDTO.Action::class.java)

                viewModel.setTime(action.remainTime)
                when (action.type) {
                    ActionType.VERTICAL.ordinal -> {
                        viewModel.addVerticalWall(Coordinate(action.row, action.col))
                        viewModel.turnPass()
                    }
                    ActionType.HORIZONTAL.ordinal -> {
                        viewModel.addHorizontalWall(Coordinate(action.row, action.col))
                        viewModel.turnPass()
                    }
                    ActionType.MOVE.ordinal -> {
                        viewModel.move(Coordinate(action.row, action.col))
                        viewModel.turnPass()
                    }
                    ActionType.WIN.ordinal -> {
                        gameEnd(opTurn)
                    }
                    ActionType.LOSE.ordinal -> {
                        gameEnd(myTurn)
                    }
                    -1 -> {
                        val rating = action.remainTime
                        UserManager.umscore = rating.toInt()
                    }
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d(TAG, "web socket closing\n$code\n$reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d(TAG, "web socket closed\n$code\n$reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d(TAG, "web socket fail\n${t.message}")
            }

        }
        ws = client.newWebSocket(request, listener)
        send(WebSocketDTO.Action(9, 9, 9, 9))
    }

    private fun makeMessage(type: ActionType, coordinate: Coordinate): WebSocketDTO.Action{
        val myData = viewModel.getPlayerValue(myTurn)
        return WebSocketDTO.Action(
            myData.leftTime,
            type.ordinal,
            coordinate.r,
            coordinate.c)
    }
    private fun makeMessage(): WebSocketDTO.Action {
        val myData = viewModel.getPlayerValue(myTurn)
        return WebSocketDTO.Action(
            myData.leftTime,
            ActionType.LOSE.ordinal,
            0,
            0)
    }

    private fun send(action: WebSocketDTO.Action) {
        CoroutineScope(Dispatchers.IO)
            .launch {
                Log.d(TAG, "ws send ${gson.toJson(action)}")
                ws.send(gson.toJson(action))
            }
    }
}