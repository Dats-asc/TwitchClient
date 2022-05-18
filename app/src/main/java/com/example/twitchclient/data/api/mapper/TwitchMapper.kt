package com.example.twitchclient.data.api.mapper

import com.example.twitchclient.data.responses.games.GamesResponse
import com.example.twitchclient.data.responses.twitch.channels.SearchChannelsResponse
import com.example.twitchclient.data.responses.twitch.emotes.TwitchGlobalEmotesResponse
import com.example.twitchclient.data.responses.twitch.stream.StreamsResponse
import com.example.twitchclient.data.responses.twitch.user.UserResponse
import com.example.twitchclient.domain.entity.emotes.twitch.Emote
import com.example.twitchclient.domain.entity.emotes.twitch.TwitchGlobalEmotes
import com.example.twitchclient.domain.entity.search.ChannelInfo
import com.example.twitchclient.domain.entity.search.Channels
import com.example.twitchclient.domain.entity.search.GameInfo
import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.entity.user.User

class TwitchMapper {

    fun mapUserResponse(response: UserResponse): User {
        return with(response.data.first()) {
            User(
                id = this.id,
                login = this.login,
                display_name = this.display_name,
                description = this.description,
                profile_image_url = this.profile_image_url,
                view_count = this.view_count,
                created_at = this.created_at
            )
        }
    }

    fun mapStreamResponse(response: StreamsResponse): Streams {
        val streamList = arrayListOf<com.example.twitchclient.domain.entity.streams.StreamData>()
        response.data.forEach { stream ->
            streamList.add(StreamData(
                id = stream.id,
                user_id = stream.id,
                user_login = stream.user_login,
                user_name = stream.user_name,
                game_id = stream.game_id,
                game_name = stream.game_name,
                type = stream.type,
                title = stream.title,
                viewer_count = stream.viewer_count,
                started_at = stream.started_at,
                language = stream.language,
                thumbnail_url = with(stream.thumbnail_url) {
                    this.replace("{width}", "640")
                        .replace("{height}", "360")
                }
            ))
        }

        return Streams(streams = streamList, cursor = response.pagination.cursor.orEmpty())
    }

    fun mapTwitchGlobalEmotesResponse(response: TwitchGlobalEmotesResponse): TwitchGlobalEmotes {
        val mappedEmoteList = mutableListOf<Emote>()
        response.data.forEach { emoteData ->
            mappedEmoteList.add(
                Emote(
                    id = emoteData.id,
                    format = emoteData.format,
                    images = emoteData.images,
                    name = emoteData.name,
                    scale = emoteData.scale,
                    theme_mode = emoteData.theme_mode
                )
            )
        }

        return TwitchGlobalEmotes(
            emotes = mappedEmoteList,
            template = response.template
        )
    }

    fun mapSearchChannelResponse(response: SearchChannelsResponse): Channels {
        val mappedList = arrayListOf<ChannelInfo>()
        response.data.forEach { channel ->
            mappedList.add(
                ChannelInfo(
                    broadcaster_login = channel.broadcaster_login,
                    display_name = channel.display_name,
                    id = channel.id,
                    game_id = channel.game_id,
                    is_live = channel.is_live,
                    thumbnail_url = channel.thumbnail_url
                )
            )
        }
        return Channels(mappedList, response.pagination.cursor)
    }

    fun mapGamesResponse(response: GamesResponse): Games {
        val mappedList = arrayListOf<GameInfo>()
        response.data.forEach { game ->
            mappedList.add(
                GameInfo(
                    id = game.id,
                    name = game.name,
                    box_art_url = with(game.box_art_url) {
                        this.replace("{width}", "52")
                            .replace("{height}", "72")
                    }
                )
            )
        }

        return Games(mappedList, response.pagination.cursor)
    }

    private fun mapStreamData(streamData: StreamData) =
        com.example.twitchclient.domain.entity.streams.StreamData(
            id = streamData.id,
            user_id = streamData.id,
            user_login = streamData.user_login,
            user_name = streamData.user_name,
            game_id = streamData.game_id,
            game_name = streamData.game_name,
            type = streamData.type,
            title = streamData.title,
            viewer_count = streamData.viewer_count,
            started_at = streamData.started_at,
            language = streamData.language,
            thumbnail_url = with(streamData.thumbnail_url) {
                this.replace("{width}", "640")
                    .replace("{height}", "360")
            }
        )
}