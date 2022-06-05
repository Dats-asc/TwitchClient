package com.example.twitchclient.data.api.mapper

import com.example.twitchclient.data.responses.games.GameResponse
import com.example.twitchclient.data.responses.games.GamesResponse
import com.example.twitchclient.data.responses.twitch.channels.SearchChannelsResponse
import com.example.twitchclient.data.responses.twitch.emotes.TwitchGlobalEmotesResponse
import com.example.twitchclient.data.responses.twitch.stream.StreamsResponse
import com.example.twitchclient.data.responses.twitch.user.UserResponse
import com.example.twitchclient.data.responses.twitch.videos.VideosResponse
import com.example.twitchclient.domain.entity.emotes.twitch.Emote
import com.example.twitchclient.domain.entity.emotes.twitch.TwitchGlobalEmotes
import com.example.twitchclient.domain.entity.search.ChannelInfo
import com.example.twitchclient.domain.entity.search.Channels
import com.example.twitchclient.domain.entity.search.GameInfo
import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.entity.user.User
import com.example.twitchclient.domain.entity.user.UserDetail
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.entity.videos.Videos

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

    fun mapStreamResponse(streamsResp: StreamsResponse, usersResp: UserResponse): Streams {
        val streamList = arrayListOf<com.example.twitchclient.domain.entity.streams.StreamData>()
        streamsResp.data.forEach { stream ->
            var user = usersResp.data.find { user -> user.id == stream.user_id }
            streamList.add(
                StreamData(
                    id = user?.id.orEmpty(),
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
                    },
                    profile_image = user?.profile_image_url.orEmpty()
                )
            )
        }
        return Streams(streams = streamList, cursor = streamsResp.pagination.cursor.orEmpty())
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

    fun mapGameResponse(response: GameResponse): Games {
        val mappedList = arrayListOf<GameInfo>()
        response.data.forEach { game ->
            mappedList.add(
                GameInfo(
                    id = game.id,
                    name = game.name,
                    box_art_url = with(game.box_art_url) {
                        this.replace("{width}", "128")
                            .replace("{height}", "164")
                    }
                )
            )
        }

        return Games(mappedList, "")
    }

    fun mapUserDetail(user: UserResponse, streams: StreamsResponse?): UserDetail {
        var startedAt = ""
        var viewerCount = -1
        var isLive = false
        streams?.let {
            val a = 0
            if (it.data.size != 0) {
                val a = 0
                startedAt = it.data[0].started_at
                viewerCount = it.data[0].viewer_count
                isLive = true
            }
        }
        val user = user.data[0]
        return UserDetail(
            id = user.id,
            created_at = user.created_at,
            started_at = startedAt,
            display_name = user.display_name,
            description = user.description,
            login = user.login,
            offline_image_url = user.offline_image_url,
            profile_image_url = user.profile_image_url,
            view_count = user.view_count,
            viewer_count = viewerCount,
            is_live = isLive,
            broadcaster_type = user.broadcaster_type
        )
    }

    fun mapVideosResponse(videos: VideosResponse): Videos {
        val mappedList = arrayListOf<VideoInfo>()
        videos.data.forEach { video ->
            mappedList.add(
                VideoInfo(
                    id = video.id,
                    userId = video.user_id,
                    userLogin = video.user_login,
                    title = video.title,
                    description = video.description,
                    createdAt = video.created_at,
                    publishedAt = video.published_at,
                    url = video.url,
                    previewUrl = with(video.thumbnail_url) {
                        this.replace("%{width}", "640")
                            .replace("%{height}", "360")
                    },
                    viewCount = video.view_count,
                    duration = video.duration,
                    hlsUrl = null
                )
            )
        }
        return Videos(videos = mappedList, cursor = videos.pagination.cursor)
    }
}