package edu.auburn.eng.csse.comp3710.team8;

/**
 * NOTES: Okay so I really don't have any good suggestions for exactly how this class will
 * be implemented. I'm starting with static methods so that we can access the data
 * without needing to instantiate the Helper -- this COULD be a terrible way to do it though.
 *
 */
public class PaletteStorageHelper {

    /* I'll definitely need this method, as this'll be called by PaletteDetailsActivity
     * when the "Save Palette to Favorites" button is pressed.
     */
    public static boolean save(Palette p) {
        // Add palette to favorites

        return true; // Return true for success
    }

    /* Definitely needed so that users can remove palettes from their favorites. I'm also
     * considering making this callable as a sort of anti-button within PaletteDetailsActivity
     * i.e. If the palette hasn't been saved, Save it. If it HAS been saved, remove it.
     *      (See isSaved() comments)
     */
    public static boolean remove(Palette p) {
        // Remove palette from favorites

        return true; // Return true for success
    }

    /* While not strictly necessary, this could be useful for checking if we're trying to save
     * an identical palette / seeing if a palette has already been saved.
     */
    public static boolean isSaved(Palette p) {
        // Check if palette has already been inserted into favorites

        return true; // Return true for success

    }

    /* TOP-TIER IMPORTANCE: PaletteAdapter REQUIRES a Palette[] in order to function,
     * and I'll be giving a PaletteAdapter to FavoritePalettesActivity to populate
     * the ListView.
     */
    public static Palette[] getAllPalettes() {
        return null;
    }

    /* Again, this is just a rough outline for this interface. I've given my input as a sort
     * of hypothetical, but if you end up needing to make some changes it won't be a big deal.
     * However you can make it work is good for me. -- Patrick
     */

}
