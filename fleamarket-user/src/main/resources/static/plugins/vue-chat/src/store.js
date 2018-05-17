/**
 * Vuex
 * http://vuex.vuejs.org/zh-cn/intro.html
 */
import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

$(function () {

});
function loadSessions() {
    $.get(contextPath + 'letter/sessions', {}, function (data) {
        for(let i = 0; i < data.length; i++){
            let user = data[i].user;
            let letter = data[i].letter;
            store.sessions.push({
                id: user.id,
                user: {
                    name: user.nickname,
                    img: contextPath + 'file?path=' + user.photo
                },
                messages: []
            })
        }
    }, 'json');
}
function initWebSocket() {
    let webSocket;
    if((typeof WebSocket) == 'undefined'){
        console.log('您的浏览器不支持WebSocket');
    }else{
        webSocket = new WebSocket('ws://localhost:8080/user/ws');
        webSocket.onopen = function () {
            console.log("Socket已打开");
        };
        webSocket.onclose = function() {
            console.log("Socket已关闭");
        };
        //发生了错误事件
        webSocket.onerror = function() {
            alert("Socket发生了错误");
        };
        $(window).unload(function(){
            webSocket.close();
        });
        webSocket.onmessage = function (message) {
            let messageWrap = JSON.parse(message);
            message.target
        }
    }
}

const now = new Date();
const store = new Vuex.Store({
    state: {
        // 当前用户
        user: {
            name: 'coffce',
            img: 'dist/images/1.jpg'
        },
        // 会话列表
        sessions: [
            {
                id: 1,
                user: {
                    name: '示例介绍',
                    img: 'dist/images/2.png'
                },
                messages: [
                    {
                        content: 'Hello，这是一个基于Vue + Vuex + Webpack构建的简单chat示例，聊天记录保存在localStorge, 有什么问题可以通过Github Issue问我。',
                        date: now
                    }, {
                        content: '项目地址: https://github.com/coffcer/vue-chat',
                        date: now
                    }
                ]
            },
            {
                id: 2,
                user: {
                    name: 'webpack',
                    img: 'dist/images/3.jpg'
                },
                messages: []
            }
        ],
        // 当前选中的会话
        currentSessionId: 1,
        // 过滤出只包含这个key的会话
        filterKey: ''
    },
    mutations: {
        INIT_DATA (state) {
            let data = localStorage.getItem('vue-chat-session');
            if (data) {
                state.sessions = JSON.parse(data);
            }
        },
        // 发送消息
        SEND_MESSAGE ({ sessions, currentSessionId }, content) {
            let session = sessions.find(item => item.id === currentSessionId);
            // gss
            $.get(contextPath + 'letter/sessions',{},function (data) {
                for(var i = 0; i < data.length; i++){
                    var targetUser = data[i].user;
                    var letter = data[i].letter;

                }
            },'json');

            session.messages.push({
                content: content,
                date: new Date(),
                self: true
            });
        },
        // 选择会话
        SELECT_SESSION (state, id) {
            state.currentSessionId = id;
        } ,
        // 搜索
        SET_FILTER_KEY (state, value) {
            state.filterKey = value;
        }
    }
});

store.watch(
    (state) => state.sessions,
    (val) => {
        console.log('CHANGE: ', val);
        localStorage.setItem('vue-chat-session', JSON.stringify(val));
    },
    {
        deep: true
    }
);

export default store;
export const actions = {
    initData: ({ dispatch }) => dispatch('INIT_DATA'),
    sendMessage: ({ dispatch }, content) => dispatch('SEND_MESSAGE', content),
    selectSession: ({ dispatch }, id) => dispatch('SELECT_SESSION', id),
    search: ({ dispatch }, value) => dispatch('SET_FILTER_KEY', value)
};
