package com.example.quoridor.ingame

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.core.view.children
import com.example.quoridor.R
import com.example.quoridor.databinding.ActivityGame1v1Binding
import java.util.LinkedList
import java.util.Queue
import kotlin.reflect.typeOf

class Game1v1 : ComponentActivity() {
//    lateinit var binding: ActivityGame1v1Binding

    private var pieces = Array<Array<LinearLayout?>>(9){Array<LinearLayout?>(9){null} }
    private var vertWalls = Array<Array<View?>>(8){Array<View?>(8){null} }
    private var horiWalls = Array<Array<View?>>(8){Array<View?>(8){null} }

    private val dr = arrayOf(1,0,-1,0)
    private val dc = arrayOf(0,1,0,-1)

    private val binding: ActivityGame1v1Binding by lazy {
        ActivityGame1v1Binding.inflate(layoutInflater)
    }

    private var turn: Int = 0

    private val player0: Player = Player(0, 4, 0)
    private val player1: Player = Player(8, 4, 1)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        for (v in binding.root.children){
            if (v is LinearLayout){
                val tag = v.tag as? String
                if(tag != null && tag.length > 2) {
                    val row = tag.get(1).digitToInt()
                    val col = tag.get(2).digitToInt()
                    pieces[row][col] = v
                }
                v.setOnClickListener{
                    pieceClick(v)
                }
            }
            else {
                val tag = v.tag as? String
                if(tag != null && tag.length > 2){
                    val row = tag.get(1).digitToInt()
                    val col = tag.get(2).digitToInt()
                    when(tag.get(0)) {
                        'v' -> {
                            vertWalls[row][col] = v
                        }
                        'h' -> {
                            horiWalls[row][col] = v
                        }
                    }
                }
            }
        }


    }

    fun pieceClick(v:LinearLayout){

    }

    fun canMove(fr: Int, fc: Int, tr: Int, tc: Int, dir: Int): Boolean{
        if (tr > 8 || tr < 0 || tc > 8 || tc < 0) return false
        when(dir){
            0 -> { // 아래
                if ( (fc < 8 && horiWalls[fr][fc]!!.visibility == View.VISIBLE) || (fc > 0 && horiWalls[fr][fc-1]!!.visibility == View.VISIBLE) )
                    return false
                return true
            }
            1 -> { // 오른쪽
                if ( (fr < 8 && vertWalls[fr][fc]!!.visibility == View.VISIBLE) || (fr > 0 && vertWalls[fr-1][fc]!!.visibility == View.VISIBLE) )
                    return false
                return true
            }
            2 -> { // 위
                if ( (fc < 8 && horiWalls[fr-1][fc]!!.visibility == View.VISIBLE) || (fc > 0 && horiWalls[fr-1][fc-1]!!.visibility == View.VISIBLE) )
                    return false
                return true
            }
            3 -> { // 왼쪽
                if ( (fr < 8 && vertWalls[fr][fc-1]!!.visibility == View.VISIBLE) || (fr > 0 && vertWalls[fr-1][fc-1]!!.visibility == View.VISIBLE) )
                    return false
                return true
            }
        }
        return false
    }
    fun BFS(nodeRow : Int, nodeCol : Int) {
        val que : Queue<Pair<Int, Int>> = LinkedList<Pair<Int, Int>>()
        val visit = Array<Array<Boolean>>(9){ Array<Boolean>(9){false} }
        que.offer(Pair<Int, Int>(nodeRow, nodeCol))
        visit[nodeRow][nodeCol] = true

        while( !que.isEmpty() ) {
            var nodeNum = que.poll()

            val cr = nodeNum.first
            val cc = nodeNum.second

            for (i in 0..3 step 1) {
                val nr = cr + dr[i]
                val nc = cc + dc[i]

                if(canMove(cr, cc, nr, nc, i)){
                    que.offer(Pair<Int, Int>(nr, nc))
                    visit[nr][nc] = true
                }
            }
        }

        que.clear()
    }
}