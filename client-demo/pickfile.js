/**
 * FilePicker class is used to pick a random file from a folder. 
 */
class FilePicker {
    constructor(folder) {
        const fs = require('fs');
        this.files = fs.readdirSync(folder).map(file => {
            return {
                blob: new Blob([fs.readFileSync(folder + '/' + file)]),
                blobName: file
            }
        });
    }

    /**
     * Returns the number of files in the folder
     * @returns {number} Number of files in the folder
     */
    size() {
        return this.files.length;
    }

    /**
     * Picks a random file from the folder
     * @returns {Object} A random file from the folder. The object contains the blob and the blob name. i.e. {blob: Blob, blobName: string}
     */
    pickRandom() {
        return this.files[Math.floor(Math.random() * this.files.length)];
    }
}

module.exports = { FilePicker };