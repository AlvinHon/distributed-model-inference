const API = require('./api');
const { FilePicker } = require('./pickfile');

const loop = async (fn, timeoutMs) => {
    while (true) {
        if (await fn())
            break;
        if (timeoutMs)
            await new Promise(resolve => setTimeout(resolve, timeoutMs))
    }
}

(async () => {
    // Get the UUID from the registering the service
    let uuid = await API.getRegistryUUID();
    console.log("uuid:", uuid);

    // Load the images from the folder
    let picker = new FilePicker('images');
    console.log("files:", picker.size());

    // Loop to post images
    var post_seq = 0;
    let loopPostImage = loop(async () => {
        let file = picker.pickRandom();
        var err = await API.postImage(uuid, post_seq, 3, file);
        if (err) {
            console.log(err);
            return true;
        }
        post_seq++;
        if (post_seq % 10 == 0)
            console.log("post seq:", post_seq);
        return false;
    }, 100);

    // Loop to get results
    var get_seq = 0;
    var window_size = 3;
    var last_seq = -1;
    let loopGetResult = loop(async () => {
        let data = await API.getResult(uuid, get_seq, get_seq + window_size);

        data.items.forEach(item => {
            if (item.seq > get_seq)
                get_seq = item.seq;

            if (item.seq != last_seq)
                console.log("seq:", item.seq, item.predictions);

            last_seq = item.seq;
        });

        return false;
    }, 100);

    await loopPostImage;
    await loopGetResult;

})();
