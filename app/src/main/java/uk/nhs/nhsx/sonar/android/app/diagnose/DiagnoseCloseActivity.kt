/*
 * Copyright © 2020 NHSX. All rights reserved.
 */

package uk.nhs.nhsx.sonar.android.app.diagnose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_at_risk.nhs_service
import kotlinx.android.synthetic.main.activity_review_close.close_review_btn
import kotlinx.android.synthetic.main.symptom_banner.close_btn
import uk.nhs.nhsx.sonar.android.app.BaseActivity
import uk.nhs.nhsx.sonar.android.app.R
import uk.nhs.nhsx.sonar.android.app.appComponent
import uk.nhs.nhsx.sonar.android.app.status.StateStorage
import uk.nhs.nhsx.sonar.android.app.status.navigateTo
import uk.nhs.nhsx.sonar.android.app.util.NHS_SUPPORT_PAGE
import uk.nhs.nhsx.sonar.android.app.util.openUrl
import javax.inject.Inject

class DiagnoseCloseActivity : BaseActivity() {
    @Inject
    protected lateinit var stateStorage: StateStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        setContentView(R.layout.activity_review_close)

        close_btn.setImageDrawable(getDrawable(R.drawable.ic_arrow_back))
        close_btn.setOnClickListener {
            onBackPressed()
        }

        close_review_btn.setOnClickListener {
            navigateTo(stateStorage.get())
        }

        nhs_service.setOnClickListener {
            openUrl(NHS_SUPPORT_PAGE)
        }
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(getIntent(context))

        private fun getIntent(context: Context) =
            Intent(context, DiagnoseCloseActivity::class.java)
    }
}
