package com.harsh.shah.saavnmp3.records;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record GlobalSearch(
        @SerializedName("success") boolean success,
        @SerializedName("data") List<Data> data
) {
    public GlobalSearch {}

    public static record Data(
            @SerializedName("topQuery") TopQuery topQuery,
            @SerializedName("songs") Songs songs,
            @SerializedName("albums") Albums albums,
            @SerializedName("artists") Artists artists,
            @SerializedName("playlists") Playlists playlists
    ){
        public static record TopQuery(
                @SerializedName("results") List<Results> results,
                @SerializedName("position") int position
        ){
            public static record Results(
                    @SerializedName("id") String id,
                    @SerializedName("title") String title,
                    @SerializedName("image") List<Image> image,
                    @SerializedName("url") String url,
                    @SerializedName("type") String type,
                    @SerializedName("description") String description
            ){
                public static record Image(
                        @SerializedName("quality") String quality,
                        @SerializedName("url") String url
                ){ }
            }

        }

        public static record Songs(
                @SerializedName("results") List<Results> results,
                @SerializedName("position") int position
        ){
            public static record Results(
                    @SerializedName("id") String id,
                    @SerializedName("title") String title,
                    @SerializedName("image") List<Image> image,
                    @SerializedName("album") String album,
                    @SerializedName("url") String url,
                    @SerializedName("type") String type,
                    @SerializedName("description") String description,
                    @SerializedName("primaryArtists") String primaryArtists,
                    @SerializedName("singers") String singers,
                    @SerializedName("language") String language

            ){
                public static record Image(
                        @SerializedName("quality") String quality,
                        @SerializedName("url") String url
                ){ }
            }
        }

        public static record Albums(
                @SerializedName("results") List<Results> results,
                @SerializedName("position") int position
        ){
            public static record Results(
                    @SerializedName("id") String id,
                    @SerializedName("title") String title,
                    @SerializedName("image") List<Image> image,
                    @SerializedName("artist") String artist,
                    @SerializedName("url") String url,
                    @SerializedName("type") String type,
                    @SerializedName("description") String description,
                    @SerializedName("year") String year,
                    @SerializedName("songIds") String songIds,
                    @SerializedName("language") String language
            ){
                public static record Image(
                        @SerializedName("quality") String quality,
                        @SerializedName("url") String url
                ){ }
            }
        }

        public static record Artists(
                @SerializedName("results") List<Results> results,
                @SerializedName("position") int position
        ){
            public static record Results(
                    @SerializedName("id") String id,
                    @SerializedName("title") String title,
                    @SerializedName("image") List<Image> image,
                    @SerializedName("type") String type,
                    @SerializedName("description") String description,
                    @SerializedName("position") boolean position

            ){
                public static record Image(
                        @SerializedName("quality") String quality,
                        @SerializedName("url") String url
                ){ }

            }
        }

        public static record Playlists(
                @SerializedName("results") List<Results> results,
                @SerializedName("position") int position
        ){
            public static record Results(
                    @SerializedName("id") String id,
                    @SerializedName("title") String title,
                    @SerializedName("image") List<Image> image,
                    @SerializedName("url") String url,
                    @SerializedName("type") String type,
                    @SerializedName("language") String language,
                    @SerializedName("description") String description
            ){
                public static record Image(
                        @SerializedName("quality") String quality,
                        @SerializedName("url") String url
                ){ }
            }
        }

    }


}
