package com.example.quoridor

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.quoridor.communication.Statics
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.socket.WebSocketDTO
import com.example.quoridor.communication.socket.WebSocketService
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class GameForPvPActivity: GameActivity() {

    private lateinit var webSocketService: WebSocketService

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
    private var winner = 0

    override val gameBoardView: GameBoardView
        get() = binding.gameBoardView
    override val imageViewList: Array<ImageView>
        get() = ivList
    override val dragListener: GameBoardViewWallListener.DragListener
        get() = object :  GameBoardViewWallListener.DragListener{
            override fun startDrag(): Boolean {
                return if (!viewModel.isMyTurn(myTurn)){
                    popToast(this@GameForPvPActivity, "not my turn")
                    false
                }
                else if (!viewModel.isWallLeft(myTurn)) {
                    popToast(this@GameForPvPActivity, "no left wall")
                    false
                }
                else true
            }
        }
    override val dropListener: GameBoardViewWallListener.DropListener
        get() = object : GameBoardViewWallListener.DropListener {
            override fun cross(matchedView: View, wall: Wall): Boolean {
                return if (viewModel.wallCross(wall)) {
                    popToast(this@GameForPvPActivity, "cross")
                    true
                }
                else false
            }

            override fun closed(matchedView: View, wall: Wall): Boolean {
                return if (viewModel.wallClosed(wall)) {
                    popToast(this@GameForPvPActivity, "closed")
                    true
                }
                else false
            }

            override fun match(matchedView: View, wall: Wall): Boolean {
                return if (viewModel.wallMatch(wall)) {
                    popToast(this@GameForPvPActivity, "match")
                    true
                }
                else false
            }

            override fun success(matchedView: View, wall: Wall) {
                when(wall.type) {
                    WallType.VERTICAL -> {
                        viewModel.addVerticalWall(wall.coordinate)
                        webSocketService.send(makeMessage(ActionType.VERTICAL, wall.coordinate))
                    }
                    WallType.HORIZONTAL -> {
                        viewModel.addHorizontalWall(wall.coordinate)
                        webSocketService.send(makeMessage(ActionType.HORIZONTAL, wall.coordinate))
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
                    if (viewModel.isEnd()) {
                        webSocketService.send(makeMessage(ActionType.WIN, coordinate))
                        winner = myTurn
                    }
                    else {
                        webSocketService.send(makeMessage(ActionType.MOVE, coordinate))
                        viewModel.turnPass()
                    }
                }
                else{
                    popToast(this@GameForPvPActivity, "unavailable")
                }
            }
        }

    override val TAG: String
        get() = "$_TAG - GameForPvpActivity"

    override fun findIntent() {
        super.findIntent()
        matchData = intent.getMatchData()
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketService.send(makeMessage())
        webSocketService.close()
    }

    override fun initGame() {

        if (myTurn != 0) {
            gameBoardView.apply {
                rotation = 180F
            }
        }

        val player0 = Player(
            if (myTurn == 0) UserManager.umname!! else matchData.opponentName!!,
            gameType.timeLimit, gameType.initWall,
            if (myTurn == 0) UserManager.umscore!! else matchData.opponentScore!!,
            true)
//        binding.myInfoView.data.postValue(player0)
        val player1 = Player(
            if (myTurn != 0) UserManager.umname!! else matchData.opponentName!!,
            gameType.timeLimit, gameType.initWall,
            if (myTurn != 0) UserManager.umscore!! else matchData.opponentScore!!,
            false)
//        binding.opPlayerInfoView.data.postValue(player1)

        if (myTurn == 0) {
            binding.myInfoView.profileImageView.setImageResource(R.drawable.hobanwoo_red)
            binding.myInfoView.data.value = player0

            binding.opPlayerInfoView.profileImageView.setImageResource(R.drawable.hobanwoo_blue)
            binding.opPlayerInfoView.data.value = player1
        }
        else {
            binding.myInfoView.profileImageView.setImageResource(R.drawable.hobanwoo_blue)
            binding.myInfoView.data.value = player1

            binding.opPlayerInfoView.profileImageView.setImageResource(R.drawable.hobanwoo_red)
            binding.opPlayerInfoView.data.value = player0
        }

        val board = Board()
        gameBoardView.data.value = board
//        gameBoardView.data.postValue(board)

        viewModel.board.value = board
        viewModel.players[0].value = player0
        viewModel.players[1].value = player1
        viewModel.availableMoves.value = arrayOf()
        viewModel.turn.value = 0
        viewModel.isEnd.value = false
//        viewModel.board.postValue(board)
//        viewModel.players[0].postValue(player0)
//        viewModel.players[1].postValue(player1)
//        viewModel.availableMoves.postValue(arrayOf())
//        viewModel.turn.postValue(0)
//        viewModel.isEnd.postValue(false)

        ivList[0] = createPlayerImageView(imageResourceList[0])
        ivList[1] = createPlayerImageView(imageResourceList[1])

        binding.gameBoardView.setWallChooseView(
            binding.myWallSelector.verticalWallChooseView,
            binding.myWallSelector.horizontalWallChooseView
        )

        timer = buildTimer(0) {
//            gameEnd(1)
            winner = 1
            if (viewModel.isMyTurn(myTurn))
                webSocketService.send(makeMessage())
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

        if (turn == myTurn)
            viewModel.getAvailableMoves()
        else
            viewModel.setAvailableMove(arrayOf())

        timer = buildTimer(turn) {
//            gameEnd((turn+1)%2)
            winner = (turn+1)%2
            if (turn == myTurn)
                webSocketService.send(makeMessage())
        }
    }

//    override fun gameEnd(winner: Int) {
//        super.gameEnd(winner)
//        val remainTime = viewModel.players[myTurn].value!!.leftTime
//        val cor = viewModel.board.value!!.playCoordinates[myTurn]
//        webSocketService.send(WebSocketDTO.Action(
//            remainTime,
//            ActionType.WIN.ordinal,
//            cor.r,
//            cor.c))
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val url = "${Statics.WEB_SOCKET_BASE_URL}/game/move?uid=${UserManager.umuid}&gameId=${matchData.gameId}&turn=${matchData.turn}"
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d(TAG, "web socket opened\n$response")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d(TAG, "web socket messaged\n$text")

                val action = webSocketService.fromJsonString(text, WebSocketDTO.Action::class.java)

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
                        winner = opTurn
                        viewModel.move(Coordinate(action.row, action.col))
//                        gameEnd(opTurn)
                    }
                    ActionType.LOSE.ordinal -> {
                        winner = myTurn
                        viewModel.move(Coordinate(action.row, action.col))
//                        gameEnd(myTurn)
                    }
                    -1 -> {
                        CoroutineScope(Dispatchers.Main).launch {
                            gameEnd(winner)
                        }
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
        webSocketService = WebSocketService(url, listener)
        webSocketService.send(WebSocketDTO.Action(9, 9, 9, 9))
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
        val cor = viewModel.getCoordinate(myTurn)
        return WebSocketDTO.Action(
            myData.leftTime,
            ActionType.LOSE.ordinal,
            cor.r,
            cor.c)
    }

}