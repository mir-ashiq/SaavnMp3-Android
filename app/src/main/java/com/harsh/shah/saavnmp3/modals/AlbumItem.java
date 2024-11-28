package com.harsh.shah.saavnmp3.modals;

public class AlbumItem {

    public String albumTitle;
    public String albumSubTitle;
    public String albumCover;

    public AlbumItem() {
        
    }

    public AlbumItem(String albumTitle, String albumSubTitle, String albumCover) {
        this.albumTitle = albumTitle;
        this.albumSubTitle = albumSubTitle;
        this.albumCover = albumCover;
    }

    public String getalbumTitle() {
        return albumTitle;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setalbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public String getAlbumSubTitle() {
        return albumSubTitle;
    }

    public void setAlbumSubTitle(String albumSubTitle) {
        this.albumSubTitle = albumSubTitle;
    }
}
