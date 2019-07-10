pragma solidity ^0.4.15;

contract CunZheng {
    //
    struct userData {
        address userAddress;
        bytes32 userName;
        uint userRole;
    }

    // 文件哈希存储结构

    struct File {
        // 文件ID
        uint fileId;
        //文件哈希
        bytes[] fileHash;
        //交易哈希
        bytes[] txHash;
        // 上传者地址
        address[] userAddress;
        // 上传时间
        uint[] uploadTime;
        // 文件状态
        uint status;
    }


    //
    //mapping(address => uint) address2IdMap;
    //文件哈希
    mapping(bytes => uint) fileHash2IdMap;
    //交易哈希
    mapping(bytes => uint) txHash2IdMap;
    //
    mapping(uint => File) fileId2Map;



    //合约部署者的地址
    address owner;

    function CunZheng() {
        owner = msg.sender;
    }

    mapping(address => userData) userMap; // map

    function saveUser(
        address _userAddress,
        bytes32 _userName, uint _userRole) public returns(uint) {

        if (msg.sender != owner) {
            //权限拒绝
            return CODE_PEMISSION_DENY;
        }

        userData memory user = userMap[_userAddress];
        if (user.userName != "") {
            // body..以ing存在了
            return CODE_USER_EXITED;
        }
        user.userName = _userName;
        user.userRole = _userRole;
        user.userAddress = _userAddress;
        userMap[user.userAddress] = user;

        return CODE_SUCCESS;
    }

    //自增主键
    uint FileId_id = 0;

    function saveHash(bytes _fileHash, uint _uploadTime, uint _status, uint _fileId) public returns(uint, uint) {
        userData memory user = userMap[msg.sender];

        //权限验证，只有注册用户才能调用
        if (user.userName == "") {
            return (CODE_PEMISSION_DENY, 0);
        }

        //权限验证，只有房东上传原件
        if (user.userRole != 1) {
            return (CODE_PEMISSION_DENY, 0);
        }


        //判断权限 文件哈希判断
        if (fileHash2IdMap[_fileHash] != 0) {
            //文件已经存在
            return (CODE_FILE_EXISTED, fileHash2IdMap[_fileHash]);
        }


        if (_fileId == 0) {
            FileId_id++;
        }

        File storage file = fileId2Map[FileId_id];
        TransactionAccessor hasher = TransactionAccessor(0x00000000000000000000000000000000000000fa);

        //交易哈希
        bytes memory txHash = byteConcat(hasher.getHash());
        file.txHash.push(txHash);
        file.fileHash.push(_fileHash);
        file.uploadTime.push(_uploadTime);
        file.userAddress.push(msg.sender);
        file.fileId = FileId_id;
        file.status = _status;

        txHash2IdMap[txHash] = file.fileId;
        fileHash2IdMap[_fileHash] = file.fileId;
        fileId2Map[file.fileId] = file;
        return (CODE_SUCCESS, file.fileId);
    }

    function getFileByHash(bytes _fileHash) returns(uint, uint, bytes, bytes, address, uint) {
        userData memory user = userMap[msg.sender];

        //权限验证，只有注册用户才能调用
        if (user.userName == "") {
            return (CODE_PEMISSION_DENY, 0, "", "", 0x0, 0);
        }

        //权限控制
        if (fileHash2IdMap[_fileHash] == 0) {
            //文件不存在
            return (CODE_FILE_NOT_EXITED, 0, "", "", 0x0, 0);
        }
        //取出文件
        File memory file = fileId2Map[fileHash2IdMap[_fileHash]];
        uint index;
        for (index = 0; index < file.fileHash.length; index++) {
            uint i;
            for (i = 0; i < _fileHash.length; i++) {
                if (_fileHash[i] != (file.fileHash[index])[i]) {
                    break;
                }
            }
            if (i == _fileHash.length) {
                break;
            }
        }
        return (CODE_SUCCESS, file.fileId, _fileHash, file.txHash[index], file.userAddress[index], file.uploadTime[index]);

    }

    function updateFile(uint input_fileId, bytes last_fileHash, bytes curr_fileHash, uint curr_uploadTime, uint input_status) public returns(uint, uint) {
        userData memory user = userMap[msg.sender];

        //权限验证，只有注册用户才能调用
        if (user.userName == "") {
            return (CODE_PEMISSION_DENY, 0);
        }

        if (fileHash2IdMap[last_fileHash] == 0) {
            //文件不存在
            return (CODE_FILE_NOT_EXITED, 0);
        }
        File storage file = fileId2Map[input_fileId];

        //权限验证
        //房东签署合同原件
        if (file.status == 1) {
            if (user.userRole != 1) {
                return (CODE_PEMISSION_DENY, file.status);
            }
        } else {
            //租客签署 房东签署之后的合同
            if (file.status == 2) {
                if (user.userRole != 2) {
                    return (CODE_PEMISSION_DENY, file.status);
                }
            }
        }

        if (file.status != input_status) {
            return (CODE_FILE_STATUS_ERROR, file.status);
        }

        //根据最后一次的文件Hash判断文件是否修改
        uint index = file.fileHash.length;
        uint i;
        bool flag = true;
        for (i = 0; i < last_fileHash.length; i++) {
            if (last_fileHash[i] != (file.fileHash[index - 1])[i]) {
                flag = false;
                break;
            }
        }

        if (!flag) {
            return (CODE_FILE_MODIFIED, file.status);
        }




        TransactionAccessor hasher = TransactionAccessor(0x00000000000000000000000000000000000000fa);

        //交易哈希
        bytes memory txHash = byteConcat(hasher.getHash());
        file.txHash.push(txHash);
        file.fileHash.push(curr_fileHash);
        file.uploadTime.push(curr_uploadTime);
        file.userAddress.push(msg.sender);

        if (file.status == 1) {
            file.status = 2;
        } else {
            if (file.status == 2) {
                file.status = 3;
            }
        }

        txHash2IdMap[txHash] = file.fileId;
        fileHash2IdMap[curr_fileHash] = file.fileId;
        fileId2Map[file.fileId] = file;
        return (CODE_SUCCESS, file.status);
    }



    //返回码
    uint constant CODE_SUCCESS = 0;
    uint constant CODE_USER_EXITED = 1;
    uint constant CODE_PEMISSION_DENY = 2;
    uint constant CODE_FILE_EXISTED = 3;
    uint constant CODE_FILE_NOT_EXITED = 4;
    uint constant CODE_FILE_MODIFIED = 5;
    uint constant CODE_FILE_STATUS_ERROR = 6;

    //bytes32 转bytes
    function byteConcat(bytes32 b1) internal returns(bytes) {
        bytes memory key = new bytes(64);
        for (uint i = 0; i < b1.length; ++i) {
            key[i] = b1[i];
        }
        return key;
    }

}



contract TransactionAccessor {
    function getHash() returns(bytes32);
}