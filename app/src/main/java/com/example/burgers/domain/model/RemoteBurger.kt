package com.example.burgers.domain.model

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type


data class RemoteBurger(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("images") var images: ArrayList<Image> = arrayListOf(),
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("ingredients") var ingredients: ArrayList<Ingredient> = arrayListOf(),
    @SerializedName("price") var price: Double? = null,
    @SerializedName("veg") var veg: Boolean? = null
) {
    var link: String = ""
}

data class Image(

    @SerializedName("sm") var sm: String? = null,
    @SerializedName("lg") var lg: String? = null

)

data class Ingredient(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("img") var img: String? = null

)

class RemoteBurgerDeserializer : JsonDeserializer<RemoteBurger> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RemoteBurger {
        val jsonObject = json.asJsonObject
        val gson = Gson()

        val remoteBurger = gson.fromJson(jsonObject, RemoteBurger::class.java)
        val randomImage = remoteBurger.images.random()
        remoteBurger.link = randomImage.sm ?: randomImage.lg ?: ""

        return remoteBurger
    }
}