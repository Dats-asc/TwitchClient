package com.example.twitchclient.data.api.mapper

import com.example.twitchclient.R
import com.example.twitchclient.data.responses.twitch.stream.StreamData
import com.example.twitchclient.data.responses.twitch.stream.StreamsResponse
import com.example.twitchclient.data.responses.twitch.user.UserResponse
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.entity.user.User
import java.lang.Integer.getInteger
import java.security.AccessController.getContext

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
        val streamList = mutableListOf<com.example.twitchclient.domain.entity.streams.StreamData>()
        response.data.forEach { stream ->
            streamList.add(mapStreamData(stream))
        }

        return Streams(data = streamList)
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
            thumbnail_url = with(streamData.thumbnail_url){
                this.replace("{width}", "640")
                    .replace("{height}", "360")
            }
        )
}