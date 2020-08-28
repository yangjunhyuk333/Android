package com.junhyuk.daedo.SignUp.WorkingRetrofit

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import cn.pedant.SweetAlert.SweetAlertDialog
import com.junhyuk.daedo.Application.DaedoApplication
import com.junhyuk.daedo.Main.MainActivity
import com.junhyuk.daedo.SignUp.Activity.SignUpDialog
import com.junhyuk.daedo.SignUp.Server.SignUpBody
import com.junhyuk.daedo.SignUp.Server.SignUpResponse
import com.junhyuk.daedo.SignUp.Server.SignUpService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetupRetrofit {
    //네트워크 작업
    internal fun setupRetrofit(
        email: String,
        password: String,
        userName: String,
        base64: String,
        getApplication: Application,
        context: Context
    ) {

        //로딩 다이얼로그
        val sweetAlertDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.progressHelper.barColor = Color.parseColor("#0DE930")
        sweetAlertDialog
            .setTitleText("로딩 중")
            .setCancelable(false)
        sweetAlertDialog.show()

        //signUp 서비스
        val signUpService =
            (getApplication as DaedoApplication).retrofit.create(SignUpService::class.java)

        //signUp 서비스 결과 값
        signUpService.requestSignUp(SignUpBody(email, password, userName, base64))
            .enqueue(object : Callback<SignUpResponse> {
                val signUpDialog = SignUpDialog()

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    signUpDialog.connectionFail(context, sweetAlertDialog)
                }

                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {
                    val intent = Intent(context, MainActivity::class.java)

                    signUpDialog.connectionSuccess(
                        response.code(),
                        response.message(),
                        context,
                        response.errorBody()?.string().toString(),
                        intent,
                        sweetAlertDialog
                    )

                }

            })
    }
}