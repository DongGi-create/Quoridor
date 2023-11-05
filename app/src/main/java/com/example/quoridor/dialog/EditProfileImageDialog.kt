package com.example.quoridor.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import com.example.quoridor.databinding.DialogEditProfileBinding

/*activity는 context로 casting가능 */
class EditProfileImageDialog(context: Context) : Dialog(context) {
    val binding: DialogEditProfileBinding by lazy {
        DialogEditProfileBinding.inflate(layoutInflater)
    }

    private lateinit var customDialogInterface: CustomDialogInterface
    override fun show() {
        Log.d("minseok","Dialog show!!")
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        setCancelable(true)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히도록 함

        binding.dialogTvCamera.setOnClickListener {
            customDialogInterface.onCameraClicked()
        }

        binding.dialogTvAlbum.setOnClickListener {
            customDialogInterface.onAlbumClicked()
        }

        binding.dialogTvDelphoto.setOnClickListener {
            customDialogInterface.onDelPhotoClicked()
        }
        super.show()
    }

    fun setClickListener(customDialogInterface: CustomDialogInterface){
        this.customDialogInterface = customDialogInterface
    }
}