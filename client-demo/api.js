


const getRegistryUUID = async () => {
    return await fetch('http://localhost:8080/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({})
    }).then(res => res.json()).then(data => data.uuid);
};

const postImage = async (uuid, seq, topk, { blob, blobName }) => {
    var fd = new FormData();
    fd.set('uuid', uuid);
    fd.set('seq', seq);
    fd.set('topk', topk);
    fd.set('payload', blob, blobName);

    return await fetch('http://localhost:8080/image', {
        method: 'POST',
        body: fd
    }).then(res => {
        if (res.status == 400) {
            return res.json();
        }
        return null;
    });
};

const getResult = async (uuid, seqStart, seqEnd) => {
    return await fetch('http://localhost:8080/result?uuid=' + uuid + '&seqStart=' + seqStart + '&seqEnd=' + seqEnd, {
        method: 'GET'
    }).then(res => {
        return res.json()
    }).then(data => data);
};

module.exports = {
    getRegistryUUID,
    postImage,
    getResult
};