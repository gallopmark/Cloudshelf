package com.holike.cloudshelf.activity

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.holike.cloudshelf.CurrentApp
import com.holike.cloudshelf.R
import com.holike.cloudshelf.base.HollyActivity
import com.holike.cloudshelf.bean.ProductCatalogBean
import com.holike.cloudshelf.mvp.presenter.ProductCatalogPresenter
import com.holike.cloudshelf.mvp.view.ProductCatalogView
import kotlinx.android.synthetic.main.activity_product_catalog.*
import kotlinx.android.synthetic.main.include_backtrack.*
import kotlinx.android.synthetic.main.include_miniqr_layout.*

//产品大全
class ProductCatalogActivity : HollyActivity<ProductCatalogPresenter, ProductCatalogView>(), ProductCatalogView {
    private var mItemWidth: Int = 0
    override fun getLayoutResourceId(): Int = R.layout.activity_product_catalog

    override fun setup(savedInstanceState: Bundle?) {
        super.setup(savedInstanceState)
        initLayoutParams()
        mPresenter.getPlanContentList()
    }

    private fun initLayoutParams() {
        val dp80 = getDimensionPixelSize(R.dimen.dp_80)
        val tlp = view_back.layoutParams as FrameLayout.LayoutParams
        tlp.width = dp80
        view_back.layoutParams = tlp
        val lp = miniQrLayout.layoutParams as FrameLayout.LayoutParams
        lp.width = dp80
        miniQrLayout.layoutParams = lp
        mItemWidth = ((CurrentApp.getInstance().getMaxPixels() - containerRL.paddingLeft - containerRL.paddingRight - ((rightRL.layoutParams as RelativeLayout.LayoutParams).leftMargin)
                - dp80 - resources.getDimensionPixelSize(R.dimen.dp_10) * 3) / 4f).toInt()
    }

    override fun onResponse(bean: ProductCatalogBean) {
        Glide.with(this).load(bean.miniQrUrl).apply(RequestOptions().error(R.mipmap.ic_wxacode)).into(miniQrUrlIView)
        mPresenter.setContentList(contentLayout, bean.getContentList(), mItemWidth)
    }

    override fun onFailure(failReason: String?) {
        showShortToast(failReason)
    }

    override fun onItemClick(templateId: String?, name: String?) {
        var title = getString(R.string.text_product_catalog)
        if (!name.isNullOrEmpty()) {
            title += " > $name"
        }
        MultiTypeActivity.open(this, MultiTypeActivity.TYPE_PRODUCT, R.mipmap.ic_title_products, title, templateId)
    }
}