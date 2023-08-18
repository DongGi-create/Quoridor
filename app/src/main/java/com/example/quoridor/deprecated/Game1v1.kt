//package com.example.quoridor.deprecated
//
//import android.content.ClipData
//import android.content.ClipDescription
//import android.os.Bundle
//import android.view.View
//import android.widget.LinearLayout
//import androidx.activity.ComponentActivity
//import com.example.quoridor.databinding.ActivityGame1v1Binding
//import com.example.quoridor.game.frontDomain.FrontPlayer
//import com.example.quoridor.game.util.types.WallType
//import java.util.LinkedList
//import java.util.Queue
//
//class Game1v1 : ComponentActivity() {
////    lateinit var binding: ActivityGame1v1Binding
//
//    private val pieces by lazy {
//        Array(9){Array(9){ LinearLayout(applicationContext) } }
//    }
//
//    private val walls by lazy {
//        Array(2){Array(8){Array(8){ View(applicationContext) } }}
//    }
//    private val wallCenters by lazy {
//        Array(2){Array(8){Array(8){ Pair(0F, 0F) } }}
//    }
//
//    private val vertWalls by lazy {
//        walls[WallType.Vertical.ordinal]
//    }
//    private val horiWalls by lazy {
//        walls[WallType.Horizontal.ordinal]
//    }
//
//    private var candidateWall: View? = null
//
//    private val dr = arrayOf(1,0,-1,0)
//    private val dc = arrayOf(0,1,0,-1)
//
//    private val binding: ActivityGame1v1Binding by lazy {
//        ActivityGame1v1Binding.inflate(layoutInflater)
//    }
//
//    private var turn: Int = 0
//    private val timeLimit: Long = 10 * 60 * 1000
//
////    private val player0 by lazy{
////        val image = ImageView(applicationContext)
////        image.setImageResource(R.drawable.hobanwoo_red)
////        image.adjustViewBounds = true
////        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
////        image.layoutParams = lp
////        FrontPlayer(image, timer0!!, 8, 4, "p0", PlayerColorType.RED)
////    }
////    private var timer0: TimeCounter? = null
////
////    private val player1 by lazy {
////        val image = ImageView(applicationContext)
////        image.setImageResource(R.drawable.hobanwoo_blue)
////        image.adjustViewBounds = true
////        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
////        image.layoutParams = lp
////        FrontPlayer(image,  timer1!!, 0, 4, "p1", PlayerColorType.BLUE)
////    }
////    private var timer1: TimeCounter? = null
//
//    private val DIRTFY_TAG = "Dritfy"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
////        for (i in 0 until binding.gameBoardLayout.childCount){
////            val v = binding.gameBoardLayout.getChildAt(i)
////            if (v is LinearLayout){
////                val tag = v.tag as? String
////                Log.d("WeGlonD", tag.toString())
////                Log.d("WeGlonD", "piece found")
////                if(tag != null && tag.length > 2) {
////                    val row = tag[1].digitToInt()
////                    val col = tag[2].digitToInt()
////                    pieces[row][col] = v
////                }
////                v.setOnClickListener{
////                    pieceClick(v)
////                }
////            }
////            else {
////                val tag = v.tag as? String
////                Log.d("WeGlonD", tag.toString())
////                if(tag != null && tag.length > 2){
////                    Log.d("WeGlonD", "wall found")
////                    val row = tag[1].digitToInt()
////                    val col = tag[2].digitToInt()
////                    var type = -1
////                    when(tag[0]) {
////                        'v' -> {
////                            type = WallType.Vertical.ordinal
////                        }
////                        'h' -> {
////                            type = WallType.Horizontal.ordinal
////                        }
////                    }
////
////                    if (type == -1) continue
////                    walls[type][row][col] = v
////                }
////            }
////        }
////        Log.d("WeGlonD", vertWalls[5][5].toString())
////
////        pieces[8][4].addView(player0.imageView)
////        timer0 = TimeCounter(this, binding.p0Timer, timeLimit, object : TimeCounterOverListener {
////            override fun timeOver() {
////
////            }
////        })
////        pieces[0][4].addView(player1.imageView)
////        timer1 = TimeCounter(this, binding.p1Timer, timeLimit, object: TimeCounterOverListener {
////            override fun timeOver() {
////
////            }
////        })
////
////        timer0?.start()
//    }
//
////    override fun onResume() {
////        super.onResume()
////
////        for (i in 0 until binding.gameBoardLayout.childCount){
////            val v = binding.gameBoardLayout.getChildAt(i)
////            if (v !is LinearLayout) {
////                val tag = v.tag as? String
////                if(tag != null && tag.length > 2){
////                    val row = tag[1].digitToInt()
////                    val col = tag[2].digitToInt()
////                    var type = -1
////                    when(tag[0]) {
////                        'v' -> {
////                            type = WallType.Vertical.ordinal
////
////                        }
////                        'h' -> {
////                            type = WallType.Horizontal.ordinal
////                        }
////                    }
////
////                    if (type == -1) continue
////
////                    v.post{
////                        val cx = v.x+v.width/2
////                        val cy = v.y+v.height/2
////                        wallCenters[type][row][col] = Pair(cx, cy)
////                        Log.d(DIRTFY_TAG, "center of ${tag[0]}$row$col: ($cx, $cy)")
////                    }
////                }
////            }
////        }
////
////        walls[WallType.Vertical.ordinal][0][0].post {
////            val short = vertWalls[0][0].width
////            val long = vertWalls[0][0].height
////
////            var lp = binding.vertWall.layoutParams
////            lp.width = short
////            lp.height = long
////            binding.vertWall.layoutParams = lp
////            Log.d(DIRTFY_TAG, "vw w:${binding.vertWall.width}, h:${binding.vertWall.height}")
////
////            lp = binding.horiWall.layoutParams
////            lp.width = long
////            lp.height = short
////            binding.horiWall.layoutParams = lp
////        }
////
////
////        binding.vertWallLayout.apply{
////            tag = "chooseVertWall"
////            setOnLongClickListener {
////                wallChooseLongClick(it)
////            }
////        }
////
////        binding.horiWallLayout.apply {
////            tag = "chooseHoriWall"
////            setOnLongClickListener {
////                wallChooseLongClick(it)
////            }
////        }
////
////        binding.gameBoardLayout.setOnDragListener { _, dragEvent ->
////            when(dragEvent.action){
////                DragEvent.ACTION_DRAG_STARTED -> {
////                    dragEvent.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
////                }
////
////                DragEvent.ACTION_DRAG_ENTERED -> true
////
////                DragEvent.ACTION_DRAG_LOCATION -> {
////                    if (candidateWall != null) {
////                        candidateWall!!.visibility = View.INVISIBLE
////                        candidateWall = null
////                    }
////
////                    val x = dragEvent.x
////                    val y = dragEvent.y
////
////                    Log.d("Dirtfy", "DRAG_LOCATION: ($x, $y)")
////
////                    var wallType = 0
////                    when((dragEvent.localState as View).tag){
////                        "chooseVertWall" -> {
////                            wallType = WallType.Vertical.ordinal
////                        }
////                        "chooseHoriWall" -> {
////                            wallType = WallType.Horizontal.ordinal
////                        }
////                    }
////
////                    val wallCenters = wallCenters[wallType]
////                    val walls = walls[wallType]
////
////                    var minDis = -1F
////                    var nearestPair = Pair(0, 0)
////                    for (r in 0 .. 7){
////                        for (c in 0 .. 7){
////                            val distance = Calc.distance(Pair(x, y), wallCenters[r][c])
////                            if (minDis == -1F || distance < minDis){
////                                minDis = distance
////                                nearestPair = Pair(r, c)
////                            }
////                        }
////                    }
////
////                    if (walls[nearestPair.first][nearestPair.second].visibility != View.VISIBLE){
////                        candidateWall = walls[nearestPair.first][nearestPair.second]
////                        candidateWall!!.visibility = View.VISIBLE
////                    }
////
////                    true
////                }
////
////                DragEvent.ACTION_DROP -> {
////                    if (candidateWall != null){
////                        candidateWall!!.visibility = View.VISIBLE
////                        candidateWall = null
////
////                        if (turn == 0){
////                            timer0?.pause()
////                            timer1?.start()
////                            turn = 1
////                        }
////                        else{
////                            timer1?.pause()
////                            timer0?.start()
////                            turn = 0
////                        }
////                    }
////
////                    true
////                }
////
////                DragEvent.ACTION_DRAG_EXITED -> {
////                    if (candidateWall != null) {
////                        candidateWall!!.visibility = View.INVISIBLE
////                        candidateWall = null
////                    }
////
////                    true
////                }
////
////                DragEvent.ACTION_DRAG_ENDED -> {
////                    Log.d(DIRTFY_TAG, "DRAG_ENDED ${dragEvent.result}")
////
////                    true
////                }
////
////                else -> false
////            }
////        }
////    }
//
////    fun pieceClick(v:LinearLayout){
////        val row = v.tag.toString()[1].digitToInt()
////        val col = v.tag.toString()[2].digitToInt()
////
////        if (turn == 0){
////            timer0?.pause()
////            movePlayer(player0, row, col)
////            timer1?.start()
////            turn = 1
////        }
////        else {
////            timer1?.pause()
////            movePlayer(player1, row, col)
////            timer0?.start()
////            turn = 0
////        }
////    }
//
//    fun movePlayer(player: FrontPlayer, row: Int, col: Int){
//        val pre_row = player.row
//        val pre_col = player.col
//        pieces[pre_row][pre_col].removeAllViews()
//
//        player.row = row
//        player.col = col
//        pieces[row][col].addView(player.imageView)
//    }
//
//    fun wallChooseLongClick(view: View): Boolean {
//        val dragData = ClipData(
//            view.tag as? CharSequence,
//            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
//            ClipData.Item(view.tag as? CharSequence)
//        )
//        val shadow = View.DragShadowBuilder(view)
//
//        return view.startDragAndDrop(dragData, shadow, view, 0)
//    }
//
//    fun canMove(fr: Int, fc: Int, tr: Int, tc: Int, dir: Int): Boolean{
//        if (tr > 8 || tr < 0 || tc > 8 || tc < 0) return false
//        when(dir){
//            0 -> { // 아래
//                return !( (fc < 8 && horiWalls[fr][fc].visibility == View.VISIBLE) ||
//                          (fc > 0 && horiWalls[fr][fc-1].visibility == View.VISIBLE) )
//            }
//            1 -> { // 오른쪽
//                return !( (fr < 8 && vertWalls[fr][fc].visibility == View.VISIBLE) ||
//                          (fr > 0 && vertWalls[fr-1][fc].visibility == View.VISIBLE) )
//            }
//            2 -> { // 위
//                return !( (fc < 8 && horiWalls[fr-1][fc].visibility == View.VISIBLE) ||
//                          (fc > 0 && horiWalls[fr-1][fc-1].visibility == View.VISIBLE) )
//            }
//            3 -> { // 왼쪽
//                return !( (fr < 8 && vertWalls[fr][fc-1].visibility == View.VISIBLE) ||
//                          (fr > 0 && vertWalls[fr-1][fc-1].visibility == View.VISIBLE) )
//            }
//        }
//        return false
//    }
//    fun BFS(nodeRow : Int, nodeCol : Int) {
//        val que : Queue<Pair<Int, Int>> = LinkedList<Pair<Int, Int>>()
//        val visit = Array<Array<Boolean>>(9){ Array<Boolean>(9){false} }
//        que.offer(Pair<Int, Int>(nodeRow, nodeCol))
//        visit[nodeRow][nodeCol] = true
//
//        while( !que.isEmpty() ) {
//            var nodeNum = que.poll()
//
//            val cr = nodeNum.first
//            val cc = nodeNum.second
//
//            for (i in 0..3 step 1) {
//                val nr = cr + dr[i]
//                val nc = cc + dc[i]
//
//                if(canMove(cr, cc, nr, nc, i)){
//                    que.offer(Pair<Int, Int>(nr, nc))
//                    visit[nr][nc] = true
//                }
//            }
//        }
//
//        que.clear()
//    }
//}