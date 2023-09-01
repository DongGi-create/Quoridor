package com.example.quoridor.game

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quoridor.R
import com.example.quoridor.customView.gameBoardView.GameBoardView
import com.example.quoridor.customView.gameBoardView.GameBoardViewPieceClickListener
import com.example.quoridor.customView.gameBoardView.GameBoardViewPlayerImageGetter
import com.example.quoridor.customView.gameBoardView.GameBoardViewWallListener
import com.example.quoridor.customView.playerView.Player
import com.example.quoridor.databinding.DialogGameEndBinding
import com.example.quoridor.game.types.GameResultType
import com.example.quoridor.game.types.GameType
import com.example.quoridor.game.util.GameFunc
import com.example.quoridor.game.util.GameFunc.getGameType
import com.example.quoridor.util.Func.get
import com.example.quoridor.util.Func.removeFromParent

abstract class GameActivity: AppCompatActivity() {

    val viewModel: GameViewModel by lazy {
        ViewModelProvider(this)[GameViewModel::class.java]
    }
    abstract val gameBoardView: GameBoardView
    abstract val dragListener: GameBoardViewWallListener.DragListener
    abstract val dropListener: GameBoardViewWallListener.DropListener
    abstract val clickListener: GameBoardViewPieceClickListener

    lateinit var gameType: GameType

    val imageResourceList = arrayOf( R.drawable.baseline_lens_24_red, R.drawable.baseline_lens_24_blue)
    private var shadowImageViewList: Array<ImageView> = arrayOf()
    abstract val imageViewList: Array<ImageView>
    var timer: CountDownTimer? = null

    val _TAG by lazy {
        applicationContext.getString(R.string.Dirtfy_test_tag)
    }
    abstract val TAG: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findIntent()

        setWallSize()
        gameBoardView.setDragListener(dragListener)
        gameBoardView.setDropListener(dropListener)
        gameBoardView.setClickListener(clickListener)
        gameBoardView.setImageGetter(object : GameBoardViewPlayerImageGetter {
            override fun getImageView(playerNum: Int): ImageView {
                return imageViewList[playerNum]
            }
        })

        initGame()

        viewModel.board.observe(this) {
//            gameBoardView.data.value = it
            gameBoardView.data.postValue(it)
        }
        viewModel.players[0].observe(this) {
            player0Observe(it)
        }
        viewModel.players[1].observe(this) {
            player1Observe(it)
        }
        viewModel.availableMoves.observe(this) {
            for (iv in shadowImageViewList) {
                iv.removeFromParent()
            }

            shadowImageViewList = arrayOf()

            for(a in it){
                val playerShadow = createPlayerImageView(imageResourceList[viewModel.getTurn()])
                playerShadow.alpha = 0.5f
                shadowImageViewList += playerShadow
                gameBoardView.pieces.get(a).addView(playerShadow)
            }
        }
        viewModel.turn.observe(this) {
            turnObserve(it)
        }
//        viewModel.isEnd.observe(this) {
//            if (it) {
//                gameEnd(viewModel.turn.value!!)
//            }
//        }
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }

    override fun onBackPressed() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.D_transparent)))
        dialog.setContentView(R.layout.dialog_game_quit)

        val yesBtn = dialog.findViewById<Button>(R.id.yes_btn)
        val noBtn = dialog.findViewById<Button>(R.id.no_btn)

        yesBtn.setOnClickListener {
            dialog.dismiss()
            this.finish()
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    open fun findIntent() {
        gameType = intent.getGameType()
    }

    open fun gameEnd(winner: Int) {
        val looser = (winner+1)%2

        timer?.cancel()

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.D_transparent)))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_game_end)

        val dialogBinding = DialogGameEndBinding.bind(dialog.findViewById(R.id.top_layout))

        val winnerData = viewModel.players[winner].value!!
        val looserData = viewModel.players[looser].value!!

        dialogBinding.winnerNameTv.text = winnerData.name
        dialogBinding.winnerProfileIv.setImageResource(imageResourceList[winner])

        val player0 = viewModel.players[0].value!!
        dialogBinding.p0NameTv.text = player0.name
        dialogBinding.p0BeforeRating.text = player0.rating.toString()

        val player1 = viewModel.players[1].value!!
        dialogBinding.p1NameTv.text = player1.name
        dialogBinding.p1BeforeRating.text = player1.rating.toString()

        val winnerAfterRating = GameFunc.calcRating(winnerData.rating, looserData.rating, GameResultType.WIN)
        val looserAfterRating = GameFunc.calcRating(looserData.rating, winnerData.rating, GameResultType.LOSE)

        if (winner == 0) {
            dialogBinding.p0AfterRating.text = winnerAfterRating.toString()
            dialogBinding.p1AfterRating.text = looserAfterRating.toString()
        }
        else {
            dialogBinding.p1AfterRating.text = winnerAfterRating.toString()
            dialogBinding.p0AfterRating.text = looserAfterRating.toString()
        }

        dialogBinding.okBtn.setOnClickListener {
            dialog.dismiss()
            this.finish()
        }

        dialog.show()
    }

    fun createPlayerImageView(imageId: Int): ImageView {
        val image = ImageView(applicationContext)
        image.setImageResource(imageId)
        image.adjustViewBounds = true
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        image.layoutParams = lp
        return image
    }

    fun buildTimer(playerNum: Int, timeOver: () -> Unit = {}): CountDownTimer? {
        val playerValue = viewModel.players[playerNum].value!!
        return object : CountDownTimer(playerValue.leftTime, 100L) {
            override fun onTick(p0: Long) {
                playerValue.leftTime -= 100L
                viewModel.players[playerNum].postValue(playerValue)
//                Log.d(_TAG, "timer is running ${playerValue.leftTime}")
            }

            override fun onFinish() {
                timeOver()
            }
        }.start()
    }

    abstract fun initGame()

    abstract fun setWallSize()

    abstract fun player0Observe(player: Player)
    abstract fun player1Observe(player: Player)
    abstract fun turnObserve(turn: Int)
}