pragma solidity ^0.4.15;

contract CunZheng {
    // contract body...
    bytes32 aaa;

    //
    struct userData {
    address userAddress;
    bytes32 userName;
    uint userRole;
    }

    //合约部署者的地址
    address owner;

    function CunZheng(){
        owner=msg.sender;
    }

    mapping(address=>userData)  userMap;// map

    function saveUser (
    address _userAddress,
    bytes32 _userName,uint _userRole) public returns(uint){

        if(msg.sender!=owner){
            //权限拒绝
            return CODE_PEMISSION_DENY;
        }

        userData memory user=userMap[_userAddress];
        if(user.userName !="") {
            // body..以ing存在了
            return CODE_USER_EXITED;
        }
        user.userName=_userName;
        user.userRole=_userRole;
        user.userAddress=_userAddress;
        userMap[user.userAddress]=user;

        return CODE_SUCCESS;
    }
    //返回码
    uint constant CODE_SUCCESS=0;
    uint constant CODE_USER_EXITED=1;
    uint constant CODE_PEMISSION_DENY=2;

}