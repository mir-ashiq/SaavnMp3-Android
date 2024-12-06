package com.harsh.shah.saavnmp3.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SongResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<Song> data;

    public SongResponse() {
    }

    public SongResponse(boolean success, List<Song> data) {
        this.success = success;
        this.data = data;
    }

    // Getters and setters for success and data
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Song> getData() {
        return data;
    }

    public void setData(List<Song> data) {
        this.data = data;
    }

    public static class Song {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("type")
        private String type;
        @SerializedName("year")
        private String year;
        @SerializedName("releaseDate")
        private String releaseDate;
        @SerializedName("duration")
        private Double duration;
        @SerializedName("label")
        private String label;
        @SerializedName("explicitContent")
        private boolean explicitContent;
        @SerializedName("playCount")
        private Integer playCount;
        @SerializedName("language")
        private String language;
        @SerializedName("hasLyrics")
        private boolean hasLyrics;
        @SerializedName("lyricsId")
        private String lyricsId;
        @SerializedName("lyrics")
        private Lyrics lyrics;
        @SerializedName("url")
        private String url;
        @SerializedName("copyright")
        private String copyright;
        @SerializedName("album")
        private Album album;
        @SerializedName("artists")
        private Artists artists;
        @SerializedName("image")
        private List<Image> image;
        @SerializedName("downloadUrl")
        private List<DownloadUrl> downloadUrl;

        // Getters and setters for all properties
        // ...


        public Song() {
        }

        public Song(String id, String name, String type, String year, String releaseDate, Double duration, String label, boolean explicitContent, Integer playCount, String language, boolean hasLyrics, String lyricsId, Lyrics lyrics, String url, String copyright, Album album, Artists artists, List<Image> image, List<DownloadUrl> downloadUrl) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.year = year;
            this.releaseDate = releaseDate;
            this.duration = duration;
            this.label = label;
            this.explicitContent = explicitContent;
            this.playCount = playCount;
            this.language = language;
            this.hasLyrics = hasLyrics;
            this.lyricsId = lyricsId;
            this.lyrics = lyrics;
            this.url = url;
            this.copyright = copyright;
            this.album = album;
            this.artists = artists;
            this.image = image;
            this.downloadUrl = downloadUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public Double getDuration() {
            return duration;
        }

        public void setDuration(Double duration) {
            this.duration = duration;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public boolean isExplicitContent() {
            return explicitContent;
        }

        public void setExplicitContent(boolean explicitContent) {
            this.explicitContent = explicitContent;
        }

        public Integer getPlayCount() {
            return playCount;
        }

        public void setPlayCount(Integer playCount) {
            this.playCount = playCount;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public boolean isHasLyrics() {
            return hasLyrics;
        }

        public void setHasLyrics(boolean hasLyrics) {
            this.hasLyrics = hasLyrics;
        }

        public String getLyricsId() {
            return lyricsId;
        }

        public void setLyricsId(String lyricsId) {
            this.lyricsId = lyricsId;
        }

        public Lyrics getLyrics() {
            return lyrics;
        }

        public void setLyrics(Lyrics lyrics) {
            this.lyrics = lyrics;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public Album getAlbum() {
            return album;
        }

        public void setAlbum(Album album) {
            this.album = album;
        }

        public Artists getArtists() {
            return artists;
        }

        public void setArtists(Artists artists) {
            this.artists = artists;
        }

        public List<Image> getImage() {
            return image;
        }

        public void setImage(List<Image> image) {
            this.image = image;
        }

        public List<DownloadUrl> getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(List<DownloadUrl> downloadUrl) {
            this.downloadUrl = downloadUrl;
        }
    }

    static class Lyrics {
        @SerializedName("lyrics")
        private String lyrics;
        @SerializedName("copyright")
        private String copyright;
        @SerializedName("snippet")
        private String snippet;

        // Getters and setters for lyrics, copyright, and snippet
        // ...

        public Lyrics() {
        }

        public Lyrics(String lyrics, String copyright, String snippet) {
            this.lyrics = lyrics;
            this.copyright = copyright;
            this.snippet = snippet;
        }

        public String getLyrics() {
            return lyrics;
        }

        public void setLyrics(String lyrics) {
            this.lyrics = lyrics;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public String getSnippet() {
            return snippet;
        }

        public void setSnippet(String snippet) {
            this.snippet = snippet;
        }
    }

    static class Album {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("url")
        private String url;

        // Getters and setters for id, name, and url
        // ...

        public Album() {
        }

        public Album(String id, String name, String url) {
            this.id = id;
            this.name = name;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    static class Artists {
        @SerializedName("primary")
        private List<Artist> primary;
        @SerializedName("featured")
        private List<Artist> featured;
        @SerializedName("all")
        private List<Artist> all;

        // Getters and setters for primary, featured, and all
        // ...

        public Artists() {
        }

        public Artists(List<Artist> primary, List<Artist> featured, List<Artist> all) {
            this.primary = primary;
            this.featured = featured;
            this.all = all;
        }

        public List<Artist> getPrimary() {
            return primary;
        }

        public void setPrimary(List<Artist> primary) {
            this.primary = primary;
        }

        public List<Artist> getFeatured() {
            return featured;
        }

        public void setFeatured(List<Artist> featured) {
            this.featured = featured;
        }

        public List<Artist> getAll() {
            return all;
        }

        public void setAll(List<Artist> all) {
            this.all = all;
        }
    }

    static class Artist {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("role")
        private String role;
        @SerializedName("type")
        private String type;
        @SerializedName("image")
        private List<Image> image;
        @SerializedName("url")
        private String url;

        // Getters and setters for id, name, role, type, image, and url
        // ...

        public Artist() {
        }

        public Artist(String id, String name, String role, String type, List<Image> image, String url) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.type = type;
            this.image = image;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Image> getImage() {
            return image;
        }

        public void setImage(List<Image> image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    static class Image {
        @SerializedName("quality")
        private String quality;
        @SerializedName("url")
        private String url;

        // Getters and setters for quality and url
        // ...


        public Image() {
        }

        public Image(String quality, String url) {
            this.quality = quality;
            this.url = url;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    static class DownloadUrl {
        @SerializedName("quality")
        private String quality;
        @SerializedName("url")
        private String url;

        // Getters and setters for quality and url
        // ...

        public DownloadUrl() {
        }

        public DownloadUrl(String quality, String url) {
            this.quality = quality;
            this.url = url;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}