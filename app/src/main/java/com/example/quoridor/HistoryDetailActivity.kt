package com.example.quoridor

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quoridor.communication.retrofit.HttpSyncService
import com.example.quoridor.customView.gameBoardView.Board
import com.example.quoridor.customView.gameBoardView.GameBoardViewPlayerImageGetter
import com.example.quoridor.databinding.ActivityHistoryDetailBinding
import com.example.quoridor.game.types.ActionType
import com.example.quoridor.login.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryDetailActivity: AppCompatActivity() {

    private val binding by lazy {
        ActivityHistoryDetailBinding.inflate(layoutInflater)
    }
    private val imageResourceList = arrayOf( R.drawable.baseline_lens_24_red, R.drawable.baseline_lens_24_blue)
    private val ivList by lazy {
        Array(2) { ImageView(applicationContext) }
    }

    private val stateList = mutableListOf<Board>()
    private var stateIdx = 0

    private val TAG = "Dirtfy Test - HistoryDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        HttpSyncService.execute {
//            val historyDetail = historyDetail(intent.getLongExtra("gameId", 0))
//            binding.apply{
//                historyDetail.also {
//                    if (it == null) return@also
//                    gameIdTextView.text = it.gameId.toString()
//                    uid0TextView.text = it.uid0.toString()
//                    uid1TextView.text = it.uid1.toString()
//                    score0TextView.text = it.score0.toString()
//                    score1TextView.text = it.score1.toString()
//                    stampTextView.text = it.stamp.toString()
//                    movesTextView.text = it.moves
//                    winnerIdTextView.text = it.winnerId.toString()
//                }
//            }
//        }



        HttpSyncService.execute {
            val historyDetail = historyDetail(intent.getLongExtra("gameId", 0))

            withContext(Dispatchers.Main) {
                binding.apply {
                    historyDetail.also {
                        if (it == null) return@also

                        stampTextView.text = it.stamp.toString().substring(0, 16)

                        withContext(Dispatchers.IO) {
                            val player0Url = getImage(it.uid0)

                            withContext(Dispatchers.Main) {
                                if (player0Url == null || player0Url == "null")
                                    player0Image.setImageResource(R.drawable.ic_identity)
                                else
                                    Glide.with(this@HistoryDetailActivity)
                                        .load(player0Url)
                                        .into(player0Image)
                            }
                        }
                        player0NameTextView.text = if (it.uid0 == UserManager.umuid) {
                            UserManager.umname
                        } else {
                            intent.getStringExtra("opName")
                        }
                        player0RatingTextView.text = it.score0.toString()

                        withContext(Dispatchers.IO) {
                            val player1Url = getImage(it.uid1)

                            withContext(Dispatchers.Main) {
                                if (player1Url == null || player1Url == "null")
                                    player1Image.setImageResource(R.drawable.ic_identity)
                                else
                                    Glide.with(this@HistoryDetailActivity)
                                        .load(player1Url)
                                        .into(player1Image)
                            }
                        }
                        player1NameTextView.text = if (it.uid1 == UserManager.umuid) {
                            UserManager.umname
                        } else {
                            intent.getStringExtra("opName")
                        }
                        player1RatingTextView.text = it.score1.toString()

                        if (isMe(it.uid1)) {
                            binding.board.apply {
                                rotation = 180F
                            }
                        }

                        var turn = 0
                        var nowBoard = Board()
                        Log.d("Dirtfy HistoryDetailActivity", "moves: ${it.moves.substring(1).split('/')}")
                        for (move in it.moves.substring(1).split('/')) {
                            stateList.add(nowBoard.copy())
                            nowBoard = act(nowBoard.copy(), turn, move)
                            turn = (turn+1)%2
                        }
                        stateList.add(nowBoard.copy())

                        prevLayout.setOnClickListener {
                            if (stateIdx-1 < 0) return@setOnClickListener

                            stateIdx--
                            board.data.postValue(stateList[stateIdx])
                            Log.d("Dirtfy HistoryDetailActivity", "prev\n" +
                                    "$stateIdx\n" +
                                    "${stateList[stateIdx]}")
                        }
                        nextLayout.setOnClickListener {
                            if (stateIdx+1 > stateList.size-1) return@setOnClickListener

                            stateIdx++
                            board.data.postValue(stateList[stateIdx])
                            Log.d("Dirtfy HistoryDetailActivity", "next\n" +
                                    "$stateIdx\n" +
                                    "${stateList[stateIdx]}")
                        }

                    }
                }
            }
        }

        ivList[0] = createPlayerImageView(imageResourceList[0])
        ivList[1] = createPlayerImageView(imageResourceList[1])

        binding.board.setImageGetter(object : GameBoardViewPlayerImageGetter {
            override fun getImageView(playerNum: Int): ImageView {
                return ivList[playerNum]
            }
        })

        binding.board.data.value = Board()

    }

    private fun act(board: Board, turn: Int, action: String): Board{
        val actionType = action[0].digitToInt()
        val row = action[1].digitToInt()
        val col = action[2].digitToInt()

        when(actionType) {
            ActionType.VERTICAL.ordinal -> {
                board.verticalWalls[row][col] = true
            }
            ActionType.HORIZONTAL.ordinal -> {
                board.horizontalWalls[row][col] = true
            }
            ActionType.MOVE.ordinal, ActionType.WIN.ordinal -> {
                board.playCoordinates[turn].r = row
                board.playCoordinates[turn].c = col
            }
        }

        return board
    }

    private fun createPlayerImageView(imageId: Int): ImageView {
        val image = ImageView(applicationContext)
        image.setImageResource(imageId)
        image.adjustViewBounds = true
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        image.layoutParams = lp
        return image
    }

    private fun isMe(uid: Long): Boolean {
        return UserManager.umuid!! == uid
    }
}