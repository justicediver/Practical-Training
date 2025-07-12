# -*- coding: utf-8 -*-
from loguru import logger
from webrunnercore import wr #wr模块是webrunner内置的脚本增强sdk
from webrunnercore import *



class 测试负载(WebLoadMachine):
    '''
    用户自定义负载信息
    '''
    负载名称 = '智能投顾test'

    测试机 = [
        {
            "ip地址": '118.202.10.218',
            "端口": 50000,
            "节点数": 1,
            "主节点": True
        },
    ]

class 测试场景(WebScenario):
    '''
    用户自定义场景信息
    '''
    场景名称 = '智能投顾test'

    模式 = '梯形负载'
    参数 = {
        '用户数': 50,
        '创建速率': 10,
        '运行时长': 60
    }



class Transaction_Login(SerialTransaction):
    '''
    事务定义, 一个事务由多个task构成, 每个task只包含一个请求
    '''

    def __init__(self, parent: "User") -> None:
        super().__init__(parent)

    @property
    def transaction(self):
        # 事务名称
        return "login"

    def on_start(self):
        # 事务启动函数
        super().on_start()
    
    @task
    def task_0(self):
        url = "http://localhost:8080/login"
        headers = {'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'zh-CN,zh;q=0.9', 'Connection': 'keep-alive', 'Host': 'localhost:8080', 'Origin': 'http://localhost:5173', 'Referer': 'http://localhost:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'same-site', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36', 'sec-ch-ua': '"Not.A/Brand";v="8", "Chromium";v="114"', 'sec-ch-ua-mobile': '?0', 'sec-ch-ua-platform': '"Windows"'}
        params = {}
        params['username']='john_doe'
        params['password']='password123'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()


    def on_stop(self):
        # 事务结束函数
        super().on_stop()


class Transaction_Fundresearch(SerialTransaction):
    '''
    事务定义, 一个事务由多个task构成, 每个task只包含一个请求
    '''

    def __init__(self, parent: "User") -> None:
        super().__init__(parent)

    @property
    def transaction(self):
        # 事务名称
        return "fundResearch"

    def on_start(self):
        # 事务启动函数
        super().on_start()
    
    @task
    def task_1(self):
        url = "http://localhost:5173/src/views/FundResearch.vue"
        headers = {'Accept': '*/*', 'Accept-Language': 'zh-CN,zh;q=0.9', 'Connection': 'keep-alive', 'Host': 'localhost:5173', 'Origin': 'http://localhost:5173', 'Referer': 'http://localhost:5173/src/views/FundResearch.vue', 'Sec-Fetch-Dest': 'script', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'same-origin', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36', 'sec-ch-ua': '"Not.A/Brand";v="8", "Chromium";v="114"', 'sec-ch-ua-mobile': '?0', 'sec-ch-ua-platform': '"Windows"'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='54efbc23'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_2(self):
        url = "http://localhost:8080/fund/checkInFOF"
        headers = {'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'zh-CN,zh;q=0.9', 'Connection': 'keep-alive', 'Host': 'localhost:8080', 'Origin': 'http://localhost:5173', 'Referer': 'http://localhost:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'same-site', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36', 'sec-ch-ua': '"Not.A/Brand";v="8", "Chromium";v="114"', 'sec-ch-ua-mobile': '?0', 'sec-ch-ua-platform': '"Windows"'}
        params = {}
        params['id']='1'
        params['fundcode']='1001'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()


    def on_stop(self):
        # 事务结束函数
        super().on_stop()


class Transaction_Factormanage(SerialTransaction):
    '''
    事务定义, 一个事务由多个task构成, 每个task只包含一个请求
    '''

    def __init__(self, parent: "User") -> None:
        super().__init__(parent)

    @property
    def transaction(self):
        # 事务名称
        return "factorManage"

    def on_start(self):
        # 事务启动函数
        super().on_start()
    
    @task
    def task_3(self):
        url = "http://localhost:8080/factors/add"
        headers = {'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'zh-CN,zh;q=0.9', 'Connection': 'keep-alive', 'Content-Length': '118', 'Content-Type': 'application/json', 'Host': 'localhost:8080', 'Origin': 'http://localhost:5173', 'Referer': 'http://localhost:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'same-site', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36', 'sec-ch-ua': '"Not.A/Brand";v="8", "Chromium";v="114"', 'sec-ch-ua-mobile': '?0', 'sec-ch-ua-platform': '"Windows"'}
        params = {}
        # application/json (dict)
        data = {}
        data['dfname'] = 'yy'
        data['id'] = 1
        data['factornames'] = '52周新高,ROE,毛利率,历史波动率,市净率'
        data['percents'] = '20,30,30,10,10'

        res = self.post(url, headers=headers, params=params, json=data)
        # application/json,res.json()


    def on_stop(self):
        # 事务结束函数
        super().on_stop()


class Transaction_Strategymanage(SerialTransaction):
    '''
    事务定义, 一个事务由多个task构成, 每个task只包含一个请求
    '''

    def __init__(self, parent: "User") -> None:
        super().__init__(parent)

    @property
    def transaction(self):
        # 事务名称
        return "strategyManage"

    def on_start(self):
        # 事务启动函数
        super().on_start()
    
    @task
    def task_4(self):
        url = "http://localhost:8080/strategy/save"
        headers = {'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'zh-CN,zh;q=0.9', 'Connection': 'keep-alive', 'Content-Length': '77', 'Content-Type': 'application/json', 'Host': 'localhost:8080', 'Origin': 'http://localhost:5173', 'Referer': 'http://localhost:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'same-site', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36', 'sec-ch-ua': '"Not.A/Brand";v="8", "Chromium";v="114"', 'sec-ch-ua-mobile': '?0', 'sec-ch-ua-platform': '"Windows"'}
        params = {}
        # application/json (list)
        data = [None, None]
        data[0] = {}
        data[0]['id'] = 1
        data[0]['fundcode'] = 1001
        data[0]['percent'] = 60
        data[1] = {}
        data[1]['id'] = 1
        data[1]['fundcode'] = 1002
        data[1]['percent'] = 40

        res = self.post(url, headers=headers, params=params, json=data)
        # application/json,res.json()

    
    @task
    def task_5(self):
        url = "http://localhost:8080/strategy/updateFofname"
        headers = {'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'zh-CN,zh;q=0.9', 'Connection': 'keep-alive', 'Content-Length': '25', 'Content-Type': 'application/json', 'Host': 'localhost:8080', 'Origin': 'http://localhost:5173', 'Referer': 'http://localhost:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'same-site', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36', 'sec-ch-ua': '"Not.A/Brand";v="8", "Chromium";v="114"', 'sec-ch-ua-mobile': '?0', 'sec-ch-ua-platform': '"Windows"'}
        params = {}
        # application/json (dict)
        data = {}
        data['id'] = 1
        data['fofname'] = 'xxxx'

        res = self.post(url, headers=headers, params=params, json=data)
        # application/json,res.json()


    def on_stop(self):
        # 事务结束函数
        super().on_stop()


class Transaction_Productmanage(SerialTransaction):
    '''
    事务定义, 一个事务由多个task构成, 每个task只包含一个请求
    '''

    def __init__(self, parent: "User") -> None:
        super().__init__(parent)

    @property
    def transaction(self):
        # 事务名称
        return "productManage"

    def on_start(self):
        # 事务启动函数
        super().on_start()
    
    @task
    def task_6(self):
        url = "http://localhost:8080/product/all"
        headers = {'Accept': '*/*', 'Accept-Language': 'zh-CN,zh;q=0.9', 'Connection': 'keep-alive', 'Host': 'localhost:8080', 'Origin': 'http://localhost:5173', 'Referer': 'http://localhost:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'same-site', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36', 'sec-ch-ua': '"Not.A/Brand";v="8", "Chromium";v="114"', 'sec-ch-ua-mobile': '?0', 'sec-ch-ua-platform': '"Windows"'}
        params = {}
        params['id']='1'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()


    def on_stop(self):
        # 事务结束函数
        super().on_stop()


class Transaction_Usermanage(SerialTransaction):
    '''
    事务定义, 一个事务由多个task构成, 每个task只包含一个请求
    '''

    def __init__(self, parent: "User") -> None:
        super().__init__(parent)

    @property
    def transaction(self):
        # 事务名称
        return "userManage"

    def on_start(self):
        # 事务启动函数
        super().on_start()
    
    @task
    def task_7(self):
        url = "http://localhost:5173/src/views/UserManagement.vue"
        headers = {'Accept': '*/*', 'Accept-Language': 'zh-CN,zh;q=0.9', 'Connection': 'keep-alive', 'Host': 'localhost:5173', 'Origin': 'http://localhost:5173', 'Referer': 'http://localhost:5173/src/views/UserManagement.vue', 'Sec-Fetch-Dest': 'script', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'same-origin', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36', 'sec-ch-ua': '"Not.A/Brand";v="8", "Chromium";v="114"', 'sec-ch-ua-mobile': '?0', 'sec-ch-ua-platform': '"Windows"'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='18622dc2'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text


    def on_stop(self):
        # 事务结束函数
        super().on_stop()


class WebrunnerAction(BrowserAction):
    '''
    事务集合, 一个Action包含多个Transaction事务, @task装饰器参数表示事务混合比，
    初始化事务 和 结束事务只执行一次， 执行事务按照混合比执行多次
    '''
    def __init__(self, parent: "User") -> None:
        super().__init__(parent)

    def on_start(self):
        super().on_start()

    @task(1)
    @transaction("login")
    def task_login(self):
        # 执行事务
        Transaction_Login(self).run()

    @task(1)
    @transaction("fundResearch")
    def task_fundResearch(self):
        # 执行事务
        Transaction_Fundresearch(self).run()

    @task(1)
    @transaction("factorManage")
    def task_factorManage(self):
        # 执行事务
        Transaction_Factormanage(self).run()

    @task(1)
    @transaction("strategyManage")
    def task_strategyManage(self):
        # 执行事务
        Transaction_Strategymanage(self).run()

    @task(1)
    @transaction("productManage")
    def task_productManage(self):
        # 执行事务
        Transaction_Productmanage(self).run()

    @task(1)
    @transaction("userManage")
    def task_userManage(self):
        # 执行事务
        Transaction_Usermanage(self).run()

    def on_stop(self):
        super().on_stop()


class WebrunnerUser(CFastHttpUser):
    '''
    虚拟用户, 一个用户循环执行一个Action
    '''
    host = ""
    tasks = [WebrunnerAction]



    def __init__(self, *args, **kwargs) -> None:
        super().__init__(*args, **kwargs)

    def on_start(self):
        # 所有虚拟用户创建完成后开始执行，主要用于定义参数化和检查点的策略

        super().on_start()