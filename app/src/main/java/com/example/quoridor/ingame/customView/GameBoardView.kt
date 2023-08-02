package com.example.quoridor.ingame.customView

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.example.quoridor.R
import com.example.quoridor.databinding.GameBoardViewBinding
import com.example.quoridor.ingame.frontDomain.FrontBoard
import com.example.quoridor.ingame.utils.Calc
import com.example.quoridor.ingame.utils.WallType

class GameBoardView: ConstraintLayout {

    private val binding: GameBoardViewBinding by lazy {
        GameBoardViewBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.game_board_view, this, false)
        )
    }

    val pieces by lazy {
        Array(9){Array(9){ LinearLayout(context) } }
    }

    val walls by lazy {
        Array(2){Array(8){Array(8){ View(context) } }}
    }
    private val wallCenters by lazy {
        Array(2){Array(8){Array(8){ Pair(0F, 0F) } }}
    }

    private var recentWall: View? = null
    private var candidateWall: View? = null

    private val verticalWallTag = "verticalWall"
    private val horizontalWallTag = "horizontalWall"

    private var recentWallColor = context.getColor(R.color.recentWall)
    private var candidateWallColor = context.getColor(R.color.candidateWall)
    private var candidateMoveColor = context.getColor(R.color.candidateMove)

    private var verticalWallChooseView: View = View(context)
    private var horizontalWallChooseView: View = View(context)

    private var dragListener: GameBoardViewDropListener = object : GameBoardViewDropListener {
        override fun drop(view: View, wallType: WallType, row: Int, col: Int) {

        }
    }
    private var clickListener: GameBoardViewPieceClickListener = object: GameBoardViewPieceClickListener {
        override fun click(clickedPiece: View, row: Int, col: Int) {

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

    private fun initView() {
        addView(binding.root)

        for (v in binding.gameBoardLayout.children){
            if (v is LinearLayout){
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
                val tag = v.tag as? String
                if(tag != null && tag.length > 2){
                    val row = tag[1].digitToInt()
                    val col = tag[2].digitToInt()
                    var type = -1
                    when(tag[0]) {
                        'v' -> {
                            type = WallType.Vertical.ordinal
                        }
                        'h' -> {
                            type = WallType.Horizontal.ordinal
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
            val draggedView = dragEvent.localState as View

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

                    val x = dragEvent.x-200
                    val y = dragEvent.y-200

                    Log.d(TAG, "DRAG_LOCATION: ($x, $y)")

                    var wallType = 0
                    when((dragEvent.localState as View).tag){
                        verticalWallTag -> {
                            wallType = WallType.Vertical.ordinal
                        }
                        horizontalWallTag -> {
                            wallType = WallType.Horizontal.ordinal
                        }
                    }

                    val wallCenters = wallCenters[wallType]
                    val walls = walls[wallType]

                    var minDis = -1F
                    var nearestPair = Pair(0, 0)
                    for (r in 0 .. 7){
                        for (c in 0 .. 7){
                            val distance = Calc.distance(Pair(x, y), wallCenters[r][c])
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
                        candidateWall!!.visibility = View.VISIBLE
                        recentWall = candidateWall

                        val tag = recentWall!!.tag.toString()
                        val type = when(tag[0]){
                            'v' -> WallType.Vertical
                            else -> WallType.Horizontal
                        }
                        val row = tag[1].digitToInt()
                        val col = tag[2].digitToInt()

                        dragListener.drop(recentWall!!, type, row, col)

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
    }

    private fun setAttrs(attrs: AttributeSet){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GameBoardView)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        val boardColor = typedArray.getColor(R.styleable.GameBoardView_boardColor, context.getColor(R.color.gameBoard))
        val edgeColor = typedArray.getColor(R.styleable.GameBoardView_edgeColor, context.getColor(R.color.boundary))
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
        verticalWallChooseView.setOnLongClickListener {
            wallChooseLongClick(it)
        }
        verticalWallChooseView.tag = verticalWallTag
        this.verticalWallChooseView = verticalWallChooseView

        horizontalWallChooseView.setOnLongClickListener {
            wallChooseLongClick(it)
        }
        horizontalWallChooseView.tag = horizontalWallTag
        this.horizontalWallChooseView = horizontalWallChooseView
    }

    fun setDragListener(listener: GameBoardViewDropListener){
        dragListener = listener
    }

    fun setClickListener(listener: GameBoardViewPieceClickListener){
        clickListener = listener
    }

    fun setBoard(board: FrontBoard){
        for (r in pieces){
            for (v in r){
                v.removeAllViews()
            }
        }

        for (t in walls){
            for (r in t){
                for (v in r){
                    v.visibility = View.INVISIBLE
                }
            }
        }

        for (p in board.players){
            pieces[p.row][p.col].addView(p.imageView)
        }
        for (v in board.verticalWalls){
            walls[WallType.Vertical.ordinal][v.first][v.second].visibility = View.VISIBLE
        }
        for (h in board.horizontalWalls){
            walls[WallType.Horizontal.ordinal][h.first][h.second].visibility = View.VISIBLE
        }

        recentWall = null
    }
}