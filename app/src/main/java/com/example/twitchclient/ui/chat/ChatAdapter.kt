package com.example.twitchclient.ui.chat

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.twitchclient.domain.entity.chat.ChatMessage
import com.example.twitchclient.domain.entity.emotes.GeneralEmotes

class ChatAdapter(
    private val fragment: Fragment,
    private val messages: List<ChatMessage>,
    private val chatEmotes: GeneralEmotes,
    private val action: (Int) -> Unit
) : RecyclerView.Adapter<ChatHolder>() {

    private val defaultEmoteSize = 80
    private val badgeSize = 48
    private val emoteBuffer = mutableListOf<EmoteBufferItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatHolder = ChatHolder.create(parent, action).let {
        it
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        val spannableString = createMessageSpannableString(messages[position], holder)
        holder.bind(spannableString)
    }

    private fun createMessageSpannableString(
        chatMessage: ChatMessage,
        holder: RecyclerView.ViewHolder
    ): SpannableStringBuilder {
        val spanBuilder = SpannableStringBuilder().also {
            //TODO append user badges
            it.append(chatMessage.username)
                .setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    0,
                    chatMessage.username.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            it.append(" : ")
        }
        val emoteSpans = loadMessageSpans(chatMessage.message, spanBuilder, holder)
        emoteSpans.forEach { emoteSpan ->
            (spanBuilder as CharSequence).length
            val b = 0
            spanBuilder.setSpan(
                emoteSpan.span,
                emoteSpan.starts,
                emoteSpan.ends,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE,
            )
            (spanBuilder as CharSequence).length
            val a = 0
        }

        buildMessage(chatMessage.message, spanBuilder)

        return spanBuilder
    }

    private fun buildMessage(msg: String, builder: SpannableStringBuilder) {
        val msgParts = msg.split(' ')
        var start = builder.length
        var end = 0
        msgParts.forEach { word ->
            val emote = emoteBuffer.find { it.name.equals(word, false) }
            start += 1
            end = start + word.length
            emote?.let {
                builder.setSpan(
                    emote.span,
                    start,
                    end,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
            start += word.length
        }
    }

    private fun loadMessageSpans(
        msg: String,
        builder: SpannableStringBuilder,
        holder: RecyclerView.ViewHolder
    ): List<MessageImageSpan> {
        val imageSpans = mutableListOf<MessageImageSpan>()
        val messageParts = msg.split(' ')
        var start = builder.length
        var end = 0
        messageParts.forEach { word ->
            start += 1
            end = start + word.length

            val allEmotes =
                chatEmotes.emotes.find { it.code.equals(word, false) }
            allEmotes?.let { emote ->
                if (emote.imageType == "gif") {
                    val resource = loadGif(emote.url2x, holder)
                    resource?.let { resource ->
                        imageSpans.add(
                            MessageImageSpan(
                                name = emote.code,
                                starts = start,
                                ends = end,
                                ImageSpan(resource)
                            )
                        )
                    }
                } else {
                    val resource = loadDrawable(emote.url2x)
                    resource?.let { resource ->
                        imageSpans.add(
                            MessageImageSpan(
                                name = emote.code,
                                starts = start,
                                ends = end,
                                ImageSpan(resource)
                            )
                        )
                    }
                }
            }
            start += word.length
            builder.append(" $word")
        }
        return imageSpans
    }

    private fun loadDrawable(url: String): Drawable? {
        var drawable: Drawable? = null
        try {
            Glide.with(fragment)
                .load(url)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        val emoteSize = calculateEmoteSize(resource)
                        resource.setBounds(0, 0, emoteSize.first, emoteSize.second)
                        drawable = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        } catch (e: Exception) {
            Log.e("Glide", e.message.toString())
        }
        return drawable
    }

    private fun loadGif(url: String, holder: RecyclerView.ViewHolder): GifDrawable? {
        var drawable: GifDrawable? = null
        try {
            Glide.with(fragment)
                .asGif()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(object : CustomTarget<GifDrawable>() {
                    override fun onResourceReady(
                        resource: GifDrawable,
                        transition: Transition<in GifDrawable>?
                    ) {
                        val emoteSize = calculateEmoteSize(resource)
                        resource.apply {
                            setBounds(0, 0, emoteSize.first, emoteSize.second)
                            setLoopCount(GifDrawable.LOOP_FOREVER)
                            callback = object : Drawable.Callback {
                                override fun unscheduleDrawable(who: Drawable, what: Runnable) {
                                    holder.itemView.removeCallbacks(what)
                                }

                                override fun invalidateDrawable(who: Drawable) {
                                    holder.itemView.invalidate()
                                }

                                override fun scheduleDrawable(
                                    who: Drawable,
                                    what: Runnable,
                                    `when`: Long
                                ) {
                                    holder.itemView.postDelayed(what, `when`)
                                }
                            }
                            start()
                        }
                        drawable = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        } catch (e: Exception) {
            Log.e("Glide", e.message.toString())
        }
        return drawable
    }

    private fun calculateEmoteSize(resource: Drawable): Pair<Int, Int> {
        val ratio = resource.intrinsicWidth.toFloat() / resource.intrinsicHeight.toFloat()
        val width: Int
        val height: Int
        when {
            ratio in 0.7f..1.3f -> {
                width = defaultEmoteSize
                height = defaultEmoteSize
            }
            ratio <= 0.7f -> {
                width = defaultEmoteSize
                height = (defaultEmoteSize * ratio).toInt()
            }
            else -> {
                width = (defaultEmoteSize * ratio).toInt()
                height = defaultEmoteSize
            }
        }
        return width to height
    }

    override fun getItemCount(): Int = messages.size
}

data class EmoteBufferItem(
    val name: String,
    val span: ImageSpan
)