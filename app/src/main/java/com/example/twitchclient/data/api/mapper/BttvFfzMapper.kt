package com.example.twitchclient.data.api.mapper

import com.example.twitchclient.data.responses.bttv.BttvChanelEmotesResponse
import com.example.twitchclient.data.responses.bttv.BttvGlobalEmotesResponse
import com.example.twitchclient.data.responses.ffz.FfzChannelEmotesResponse
import com.example.twitchclient.data.responses.twitch.emotes.TwitchGlobalEmotesResponse
import com.example.twitchclient.domain.entity.emotes.GeneralEmote
import com.example.twitchclient.domain.entity.emotes.GeneralEmotes
import com.example.twitchclient.domain.entity.emotes.bttv.BttvChanelEmotes
import com.example.twitchclient.domain.entity.emotes.bttv.BttvFfzEmote
import com.example.twitchclient.domain.entity.emotes.bttv.BttvFfzGlobalEmotes
import com.example.twitchclient.domain.entity.emotes.ffz.FfzChannelEmotes
import com.example.twitchclient.domain.entity.emotes.twitch.Emote

class BttvFfzMapper {

    fun mapBttvGlobalEmotesResponse(response: BttvGlobalEmotesResponse): BttvFfzGlobalEmotes {
        val mappedList = arrayListOf<BttvFfzEmote>()
        response.forEach { emote ->
            mappedList.add(
                BttvFfzEmote(
                    id = emote.id,
                    code = emote.code,
                    imageType = emote.imageType,
                    url1x = "https://cdn.betterttv.net/emote/${emote.id}/1x",
                    url2x = "https://cdn.betterttv.net/emote/${emote.id}/2x",
                    url3x = "https://cdn.betterttv.net/emote/${emote.id}/3x"

                )
            )
        }
        return BttvFfzGlobalEmotes(emotes = mappedList)
    }

    fun mapBttvChannelEmotesResponse(response: BttvChanelEmotesResponse): BttvChanelEmotes {
        val mappedList = arrayListOf<BttvFfzEmote>()
        response.channelEmotes.forEach { channelEmote ->
            mappedList.add(
                BttvFfzEmote(
                    id = channelEmote.id,
                    code = channelEmote.code,
                    imageType = channelEmote.imageType,
                    url1x = "https://cdn.betterttv.net/emote/${channelEmote.id}/1x",
                    url2x = "https://cdn.betterttv.net/emote/${channelEmote.id}/2x",
                    url3x = "https://cdn.betterttv.net/emote/${channelEmote.id}/3x"
                )
            )
        }
        response.sharedEmotes.forEach { sharedEmote ->
            mappedList.add(
                BttvFfzEmote(
                    id = sharedEmote.id,
                    code = sharedEmote.code,
                    imageType = sharedEmote.imageType,
                    url1x = "https://cdn.betterttv.net/emote/${sharedEmote.id}/1x",
                    url2x = "https://cdn.betterttv.net/emote/${sharedEmote.id}/2x",
                    url3x = "https://cdn.betterttv.net/emote/${sharedEmote.id}/3x"
                )
            )
        }

        return BttvChanelEmotes(channelEmotes = mappedList)
    }

    fun mapFfzChannelEmotesResponse(response: FfzChannelEmotesResponse): FfzChannelEmotes{
        val mappedList = arrayListOf<BttvFfzEmote>()
        response.forEach { ffzEmote ->
            mappedList.add(
                BttvFfzEmote(
                    code = ffzEmote.code,
                    id = ffzEmote.id.toString(),
                    imageType = ffzEmote.imageType,
                    url1x = "https://cdn.betterttv.net/frankerfacez_emote/${ffzEmote.id}/1",
                    url2x = "https://cdn.betterttv.net/frankerfacez_emote/${ffzEmote.id}/2",
                    url3x = "https://cdn.betterttv.net/frankerfacez_emote/${ffzEmote.id}/3"

                )
            )
        }
        return FfzChannelEmotes(mappedList)
    }

    fun mapEmotes(
        twitchGlobalEmotes: TwitchGlobalEmotesResponse,
        bttvGlobalEmotes: BttvGlobalEmotesResponse,
        bttvChannelEmotes: BttvChanelEmotesResponse,
        ffzChannelEmotes: FfzChannelEmotesResponse
    ): GeneralEmotes {
        val emoteMappedList = arrayListOf<GeneralEmote>()

        twitchGlobalEmotes.data.forEach { emoteData ->
            emoteMappedList.add(
                GeneralEmote(
                    code = emoteData.name,
                    id = emoteData.id,
                    imageType = if (emoteData.format.contains("animated")) "gif" else "png",
                    url1x = emoteData.images.url_1x,
                    url2x = emoteData.images.url_2x,
                    url3x = emoteData.images.url_4x
                )
            )
        }

        bttvGlobalEmotes.forEach { emote ->
            emoteMappedList.add(
                GeneralEmote(
                    code = emote.code,
                    id = emote.id,
                    imageType = emote.imageType,
                    url1x = "https://cdn.betterttv.net/emote/${emote.id}/1x",
                    url2x = "https://cdn.betterttv.net/emote/${emote.id}/2x",
                    url3x = "https://cdn.betterttv.net/emote/${emote.id}/3x"
                )
            )
        }
        bttvChannelEmotes.channelEmotes.forEach { emote ->
            emoteMappedList.add(
                GeneralEmote(
                    code = emote.code,
                    id = emote.id,
                    imageType = emote.imageType,
                    url1x = "https://cdn.betterttv.net/emote/${emote.id}/1x",
                    url2x = "https://cdn.betterttv.net/emote/${emote.id}/2x",
                    url3x = "https://cdn.betterttv.net/emote/${emote.id}/3x"
                )
            )
        }
        ffzChannelEmotes.forEach { emote ->
            emoteMappedList.add(
                GeneralEmote(
                    code = emote.code,
                    id = emote.id.toString(),
                    imageType = emote.imageType,
                    url1x = "https://cdn.betterttv.net/frankerfacez_emote/${emote.id}/1",
                    url2x = "https://cdn.betterttv.net/frankerfacez_emote/${emote.id}/2",
                    url3x = "https://cdn.betterttv.net/frankerfacez_emote/${emote.id}/3"
                )
            )
        }

        return GeneralEmotes(emoteMappedList)
    }
}