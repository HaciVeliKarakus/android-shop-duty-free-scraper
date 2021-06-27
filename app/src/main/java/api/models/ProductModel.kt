package api.models

data class ProductModel(
    var brandName: String = "",
    var name: String = "",
    var price: String = "",
    var imgSrc: String = "",
    var detail: String = "",
    var infoList: ArrayList<InformationModel> = ArrayList()
)