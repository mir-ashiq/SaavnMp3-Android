package com.harsh.shah.saavnmp3.modals.apimodel;

import java.util.List;

public class GlobalSearchModel {
    private Data data;
    private boolean success;

    // Getters and Setters
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class Data {
        private Section albums;
        private Section songs;
        private Section artists;
        private Section playlists;
        private Section topQuery;

        // Getters and Setters
        public Section getAlbums() {
            return albums;
        }

        public void setAlbums(Section albums) {
            this.albums = albums;
        }

        public Section getSongs() {
            return songs;
        }

        public void setSongs(Section songs) {
            this.songs = songs;
        }

        public Section getArtists() {
            return artists;
        }

        public void setArtists(Section artists) {
            this.artists = artists;
        }

        public Section getPlaylists() {
            return playlists;
        }

        public void setPlaylists(Section playlists) {
            this.playlists = playlists;
        }

        public Section getTopQuery() {
            return topQuery;
        }

        public void setTopQuery(Section topQuery) {
            this.topQuery = topQuery;
        }
    }

    // Section class to represent albums, songs, artists, etc.
    public static class Section {
        private List<SectionItem> results;
        private int position;

        // Getters and Setters
        public List<SectionItem> getResults() {
            return results;
        }

        public void setResults(List<SectionItem> results) {
            this.results = results;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    // SectionItem class for each album, song, artist, playlist, or topQuery item
    public static class SectionItem {
        private String id;
        private String title;
        private List<Image> image;
        private String artist;
        private String url;
        private String type;
        private String description;
        private String year;
        private String language;
        private String songIds; // For songs

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Image> getImage() {
            return image;
        }

        public void setImage(List<Image> image) {
            this.image = image;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getSongIds() {
            return songIds;
        }

        public void setSongIds(String songIds) {
            this.songIds = songIds;
        }
    }

    // Image class to represent the image object
    public static class Image {
        private String quality;
        private String url;

        // Getters and Setters
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
