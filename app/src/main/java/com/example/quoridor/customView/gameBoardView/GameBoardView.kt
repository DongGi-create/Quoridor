package com.example.quoridor.customView.gameBoardView

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Outline
import android.util.AttributeSet
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.lifecycle.MutableLiveData
import com.example.quoridor.R
import com.example.quoridor.customView.ObservableView
import com.example.quoridor.databinding.CustomViewGameBoardBinding
import com.example.quoridor.util.Coordinate
import com.example.quoridor.util.Func
import com.example.quoridor.util.Func.get
import com.example.quoridor.util.Func.move

class GameBoardView: ObservableView {

    private val binding: CustomViewGameBoardBinding by lazy {
        CustomViewGameBoardBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.custom_view_game_board, this, false)
        )
    }
    val data = MutableLiveData<Board>()

    val pieces by lazy {
        Array(9){Array(9){ LinearLayout(context) } }
    }

    val walls by lazy {
        Array(2){Array(8){Array(8){ View(context) } }}
    }
    private val wallCenters by lazy {
        Array(2){Array(8){Array(8){ Pair(0F, 0F) } }}
    }

    var recentWall: View? = null
    var candidateWall: View? = null

    private val verticalWallTag = "verticalWall"
    private val horizontalWallTag = "horizontalWall"

    private var recentWallColor = context.getColor(R.color.recentWall)
    private var candidateWallColor = context.getColor(R.color.candidateWall)
    private var candidateMoveColor = context.getColor(R.color.candidateMove)

    private var verticalWallChooseView: View = View(context)
    private var horizontalWallChooseView: View = View(context)

    private var dropListener: GameBoardViewDropListener =
        object : GameBoardViewDropListener {

            override fun cross(matchedView: View, wall: Wall): Boolean {
                return true
            }

            override fun closed(matchedView: View, wall: Wall): Boolean {
                return true
            }

            override fun match(matchedView: View, wall: Wall): Boolean {
                return true
            }

            override fun success(matchedView: View, wall: Wall) {
            }
        }
    private var clickListener: GameBoardViewPieceClickListener =
        object : GameBoardViewPieceClickListener {
            override fun click(clickedPiece: View, row: Int, col: Int) {

            }
        }
    private var imageGetter: GameBoardViewPlayerImageGetter =
        object : GameBoardViewPlayerImageGetter {
            override fun getImageView(playerNum: Int): ImageView {
                return View(context) as ImageView
            }
        }

    private val TAG: String = context.getString(R.string.Dirtfy_test_tag)

    constructor(context: Context): super(context) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        initView()
        setAttrs(attrs)
    }

    private fun buildRoundOutLineProvider(round: Float): ViewOutlineProvider {
        return object: ViewOutlineProvider() {
            override fun getOutline(p0: View?, p1: Outline?) {
                p1?.setRoundRect(0, 0, p0!!.width, p0!!.height, round)
            }
        }
    }

    private fun initView() {
        addView(binding.root)

        binding.gameBoardLayout.apply {
            outlineProvider = buildRoundOutLineProvider(20F)
            clipToOutline = true
        }

        for (v in binding.gameBoardLayout.children){
            if (v is LinearLayout){
                v.apply {
                    outlineProvider = buildRoundOutLineProvider(10F)
                    clipToOutline = true
                }

                val tag = v.tag as? String
                if(tag != null && tag.length > 2) {
                    val row = tag[1].digitToInt()
                    val col = tag[2].digitToInt()

                    v.setOnClickListener {
                        clickListener.click(it, row, col)
                    }

                    pieces[row][col] = v
                }
            }
            else {
                v.apply {
                    outlineProvider = buildRoundOutLineProvider(5F)
                    clipToOutline = true
                }

                val tag = v.tag as? String
                if(tag != null && tag.length > 2){
                    val row = tag[1].digitToInt()
                    val col = tag[2].digitToInt()
                    var type = -1
                    when(tag[0]) {
                        'v' -> {
                            type = WallType.VERTICAL.ordinal
                        }
                        'h' -> {
                            type = WallType.HORIZONTAL.ordinal
                        }
                    }

                    if (type == -1) continue
                    walls[type][row][col] = v

                    v.post{
                        val cx = v.x+v.width/2
                        val cy = v.y+v.height/2
                        wallCenters[type][row][col] = Pair(cx, cy)
                    }
                }
            }
        }

        binding.gameBoardLayout.setOnDragListener { _, dragEvent ->

            when(dragEvent.action){
                DragEvent.ACTION_DRAG_STARTED -> {
                    dragEvent.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }

                DragEvent.ACTION_DRAG_ENTERED -> true

                DragEvent.ACTION_DRAG_LOCATION -> {
                    if (candidateWall != null) {
                        candidateWall!!.visibility = View.INVISIBLE
                        candidateWall = null
                    }

                    val x = dragEvent.x
                    val y = dragEvent.y

                    Log.d(TAG, "DRAG_LOCATION: ($x, $y)")

                    var wallType = 0
                    when((dragEvent.localState as View).tag){
                        verticalWallTag -> {
                            wallType = WallType.VERTICAL.ordinal
                        }
                        horizontalWallTag -> {
                            wallType = WallType.HORIZONTAL.ordinal
                        }
                    }

                    val wallCenters = wallCenters[wallType]
                    val walls = walls[wallType]

                    var minDis = -1F
                    var nearestPair = Pair(0, 0)
                    for (r in 0 .. 7){
                        for (c in 0 .. 7){
                            val distance = Func.distance(Pair(x, y), wallCenters[r][c])
                            if (minDis == -1F || distance < minDis){
                                minDis = distance
                                nearestPair = Pair(r, c)
                            }
                        }
                    }

                    if (walls[nearestPair.first][nearestPair.second].visibility != View.VISIBLE){
                        candidateWall = walls[nearestPair.first][nearestPair.second]
                        candidateWall!!.visibility = View.VISIBLE
                    }

                    true
                }

                DragEvent.ACTION_DROP -> {
                    if (candidateWall != null){

                        val tag = candidateWall!!.tag.toString()
                        val type = when(tag[0]){
                            'v' -> WallType.VERTICAL
                            else -> WallType.HORIZONTAL
                        }
                        val row = tag[1].digitToInt()
                        val col = tag[2].digitToInt()

                        val wall = Wall(type, Coordinate(row, col))
                        if (dropListener.cross(candidateWall!!, wall)) {
                            candidateWall!!.visibility = View.INVISIBLE
                        }
                        else if (dropListener.closed(candidateWall!!, wall)) {
                            candidateWall!!.visibility = View.INVISIBLE
                        }
                        else if (dropListener.match(candidateWall!!, wall)) {
                            candidateWall!!.visibility = View.VISIBLE
                        }
                        else {
                            candidateWall!!.visibility = View.VISIBLE
                            recentWall = candidateWall

                            dropListener.success(candidateWall!!, wall)
                        }

                        candidateWall = null
                    }

                    true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    if (candidateWall != null) {
                        candidateWall!!.visibility = View.INVISIBLE
                        candidateWall = null
                    }

                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    Log.d(TAG, "DRAG_ENDED ${dragEvent.result}")

                    true
                }

                else -> false
            }
        }

        data.observe {
            Log.d(TAG, "board observed")
            for (r in 0 .. 7) {
                for (c in 0 .. 7) {
                    walls[WallType.VERTICAL.ordinal][r][c].visibility =
                        if (it.verticalWalls[r][c]) View.VISIBLE  else View.INVISIBLE
                    walls[WallType.HORIZONTAL.ordinal][r][c].visibility =
                        if (it.horizontalWalls[r][c]) View.VISIBLE  else View.INVISIBLE
                }
            }

            for (i in 0 until it.playCoordinates.size) {
                val cor = it.playCoordinates[i]
                imageGetter.getImageView(i).move(pieces.get(cor))
            }
        }
    }

    private fun setAttrs(attrs: AttributeSet){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GameBoardView)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        val boardColor = typedArray.getColor(R.styleable.GameBoardView_boardColor, context.getColor(R.color.gameBoard))
        val edgeColor = typedArray.getColor(R.styleable.GameBoardView_edgeColor, context.getColor(R.color.gameBoard))
        val pieceColor = typedArray.getColor(R.styleable.GameBoardView_pieceColor, context.getColor(R.color.gameArea))
        val wallColor = typedArray.getColor(R.styleable.GameBoardView_wallColor, context.getColor(R.color.wall))
        val recentWallColor = typedArray.getColor(R.styleable.GameBoardView_recentWallColor, context.getColor(R.color.recentWall))
        val candidateWallColor = typedArray.getColor(R.styleable.GameBoardView_candidateWallColor, context.getColor(R.color.candidateWall))
        val candidateMoveColor = typedArray.getColor(R.styleable.GameBoardView_candidateMoveColor, context.getColor(R.color.candidateMove))


        binding.gameBoardLayout.setBackgroundColor(boardColor)

        binding.boardTop.setBackgroundColor(edgeColor)
        binding.boardBottom.setBackgroundColor(edgeColor)
        binding.boardStart.setBackgroundColor(edgeColor)
        binding.boardEnd.setBackgroundColor(edgeColor)

        for (r in pieces){
            for (v in r){
                v.setBackgroundColor(pieceColor)
            }
        }

        for (t in walls){
            for (r in t){
                for (v in r){
                    v.setBackgroundColor(wallColor)
                }
            }
        }

        this.recentWallColor = recentWallColor
        this.candidateWallColor = candidateWallColor
        this.candidateMoveColor = candidateMoveColor

        typedArray.recycle()
    }

    private fun wallChooseLongClick(view: View): Boolean {
        Log.d(TAG, "wallChooseLongClick")

        val dragData = ClipData(
            view.tag as? CharSequence,
            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
            ClipData.Item(view.tag as? CharSequence)
        )
        val shadowBuilder = WallShadowBuilder(view)

        return view.startDragAndDrop(dragData, shadowBuilder, view, 0)
    }

    fun setWallChooseView(verticalWallChooseView: View, horizontalWallChooseView: View){
        this.verticalWallChooseView.apply {
            tag = null
            setOnLongClickListener { true }
        }
        verticalWallChooseView.apply {
            tag = verticalWallTag
            setOnLongClickListener {
                wallChooseLongClick(it)
            }
        }
        this.verticalWallChooseView = verticalWallChooseView

        this.horizontalWallChooseView.apply {
            tag = null
            setOnLongClickListener { true }
        }
        horizontalWallChooseView.apply {
            tag = horizontalWallTag
            setOnLongClickListener {
                wallChooseLongClick(it)
            }
        }
        this.horizontalWallChooseView = horizontalWallChooseView
    }

    fun setDropListener(listener: GameBoardViewDropListener){
        dropListener = listener
    }

    fun setClickListener(listener: GameBoardViewPieceClickListener){
        clickListener = listener
    }

    fun setImageGetter(getter: GameBoardViewPlayerImageGetter) {
        this.imageGetter = getter
    }
}