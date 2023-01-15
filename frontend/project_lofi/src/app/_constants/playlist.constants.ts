export class PlaylistStatus {
    public static ACTIVATE = "Activate";
    public static DEACTIVATE = "Deactivate";
    public static RELEASED = "Released"
}

export const MplaylistGenres = new Map<string, string>([
    ['All', '01'],
    ['Jazz', '02'],
    ['Hiphop', '03'],
    ['Classic', '04'],
    ['Chill', '05'],
]);

export const MplaylistStatus = new Map<string, string>([
    ["Activate", "01"],
    ["Deactivate", "02"],
    ["Released", "03"],
]);

export const MGenresPlaylist = new Map<string, string>([
    ['01', 'All'],
    ['02', 'Jazz'],
    ['03', 'Hiphop'],
    ['04', 'Classic'],
    ['05', 'Chill'],
]);

export const MStatusPlaylist = new Map<string, string>([
    ["01", "Activate"],
    ["02", "Deactivate"],
    ["03", "Released"],
]);