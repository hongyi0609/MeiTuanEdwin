package com.rxjava

import android.util.Log
import com.rxjava.entity.SwordMan
import rx.Observable
import rx.Observer
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.functions.Func2
import rx.functions.Func3
import rx.observables.GroupedObservable
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Edwin,CHEN on 2020/5/12.
 */
open class OperatorCallManager {
    companion object {
        val TAG: String = OperatorCallManager::class.java.simpleName

        /**
         * 转换操作符
         * <p>
         *     不同于变换操作符map,转换操作符处理的是Observable对象本身，
         *     将元素以另外一种容器技术数据结构，发送给observer
         * </p>
         */
        fun toOperatorCall() {
            // toList,将元素组装成list发送，调用一次onNext
            Observable.just(1,2,4)
                    .toList()
                    .subscribe({list ->
                        Log.d(TAG,"toList:list = $list")
                    })
            // toSortedList,有序
            Observable.just(3,4,1)
                    .toSortedList({ t1, t2 ->  // 可通过Func2是想comparable接口，控制比较
                        t1 - t2
                    })
                    .subscribe({list ->
                        Log.d(TAG, "toSortedList = $list")
                    })
            // toMultimap，默认转化为HashMap发射,value是ArrayList<T>
            Observable.just(
                    SwordMan("韦一笑","A"),
                    SwordMan("张无忌","SS"),
                    SwordMan("周芷若","SS"),
                    SwordMan("张三丰","S")
            ).toMultimap({swordMan ->
                swordMan.level   // 将level作为key,封装成map
            }).subscribe({ swordManMap ->
                Log.d(TAG,"toMultimap: swordManMap's value is ${swordManMap["SS"]}")
                Log.d(TAG,"toMultimap: swordManMap's value list first element is ${(swordManMap["SS"] as ArrayList<SwordMan>)[0]}")
            })
        }
        /**
         * 条件操作符
         */
        fun conditionalOperatorCall() {
            // amb,先到先上车
            Observable.amb(
                    Observable.just(1,2,3).delay(2,TimeUnit.SECONDS), // 不会输出
                    Observable.just(4,5)
            ).subscribe({ i ->
                Log.d(TAG,"amb: i = $i")
            })

            // defaultIfEmpty,空数据时发送默认值
            Observable.create(Observable.OnSubscribe { subscriber: Subscriber<in Int>? ->
                subscriber!!.onCompleted()
            }).defaultIfEmpty(-1)
                    .subscribe({i ->
                        Log.d(TAG,"defaultIfEmpty：接口返回空，接收默认值，i= $i")
                    })

        }

        /**
         * 布尔操作符
         */
        fun booleanOperatorCall() {
            // all，将是否全部满足条件作为元素发射给observer
            Observable.just(1, 2, 3, 4)
                    .all({ integer -> // 发射所有元素是否满足条件>2的结果
                        Log.d(TAG,"all: call, t = $integer")
                        integer > 2
                    }).subscribe(object :Subscriber<Boolean>(){
                override fun onNext(t: Boolean?) {
                    Log.d(TAG,"all: t = $t")
                }

                override fun onCompleted() {
                    Log.d(TAG,"all: onCompleted")
                }

                override fun onError(e: Throwable?) {
                    Log.d(TAG,"all: onError,e = ${e!!.message}")
                }

            })
            // contains
            Observable.just(1,2,3)
                    .contains(2)
                    .subscribe({result ->
                        Log.d(TAG,"contains: result is $result")
                    })
            // isEmpty
            Observable.just(1,2,3)
                    .isEmpty
                    .subscribe({result ->
                        Log.d(TAG,"isEmpty: result is isEmpty ? $result")
                    })
        }

        /**
         * 错误处理操作符
         */
        fun catchErrorOperatorCall() {
            // onErrorReturn
            Observable.create(Observable.OnSubscribe<Int> { subscriber: Subscriber<in Int>? ->
                for (i in 0..5) {
                    if (i > 2) {
                        subscriber!!.onError(Throwable("Throwable !!!"))
                    }
                    subscriber!!.onNext(i)
                }
                subscriber!!.onCompleted()
            })
                    .onErrorResumeNext { Observable.just(1, 2) }
                    .onErrorReturn {
                        Log.d(TAG, "onErrorReturn：emit error return -1")
                        -1
                    }.subscribe(object : Observer<Int> {
                override fun onCompleted() {
                    Log.d(TAG, "onErrorReturn：onCompleted")
                }

                override fun onError(e: Throwable?) {
                    // onErrorReturn已处理，此处不会执行
                    Log.d(TAG, "onErrorReturn：onError,e = ${e!!.message}")
                }

                override fun onNext(t: Int?) {
                    Log.d(TAG, "onErrorReturn：onNext,$t")
                }
            })

            // retry 如果出问题，尝试n次请求
            Observable.create({ subscriber: Subscriber<in Int>? ->
                for (i in 0..5) {
                    if (i == 1) {
                        subscriber!!.onError(Throwable("Throwable!!!"))
                    } else {
                        subscriber!!.onNext(i)
                    }
                }
                subscriber!!.onCompleted()
            }).retry(2L) // 重试两次，共执行2次onNext,1次onError
                    .subscribe(object : Observer<Int> {
                        override fun onCompleted() {
                            Log.d(TAG, "retry：onCompleted")
                        }

                        override fun onError(e: Throwable?) {
                            // onErrorReturn已处理，此处不会执行
                            Log.d(TAG, "retry：onError,e = ${e!!.message}")
                        }

                        override fun onNext(t: Int?) {
                            Log.d(TAG, "retry：onNext,$t")
                        }
                    })
        }

        /**
         * 辅助操作符调用
         */
        fun auxiliaryOperatorCall() {
            // delay 延迟发送操作符
            Observable.create(Observable.OnSubscribe<Long> { subscriber ->
                val currentTime = System.currentTimeMillis() / 1000
                subscriber.onNext(currentTime)
            }).delay(2, TimeUnit.SECONDS).subscribe({ result ->
                val delay = System.currentTimeMillis() / 1000 - result
                Log.d(TAG, "delay,延迟发送结果，delay = $delay")
            })
            // do系列辅助操作符：doOnEach、doOnNext、doOnSubscribe、doOnUnSubscribe、doOnComplete、doOnError、
            // doOnTerminate、doAfterTerminate
            // 以doOnNext为例
            Observable.just(1, 2)
                    .doOnNext({ item ->
                        Log.d(TAG, "doOnNext: execute, item $item is called")
                    })
                    .doOnCompleted({
                        Log.d(TAG, "doOnCompleted: execute success!")
                    })
                    .doOnSubscribe({
                        Log.d(TAG, "doOnSubscribe: executed!")
                    })
                    .doOnTerminate {
                        Log.d(TAG, "doOnTerminate: executed!")
                    }
                    .doAfterTerminate {
                        Log.d(TAG, "doAfterTerminate: executed!")
                    }
                    .subscribe(
                            object : Subscriber<Int>() {
                                override fun onNext(t: Int?) {
                                    Log.d(TAG, "onNext : t = $t")
                                }

                                override fun onError(e: Throwable?) {
                                    Log.d(TAG, "onError, e = ${e!!.message}")
                                }

                                override fun onCompleted() {
                                    Log.d(TAG, "onCompleted is called")
                                }
                            }
                    )
            // subscribeOn（指定Observable运行线程）、observerOn（指定Observer运行线程）
            Observable.create(Observable.OnSubscribe<Int> { subscriber: Subscriber<in Int>? ->
                Log.d(TAG, "Observable#subscribeOn: ----Current Thread Name is ${Thread.currentThread().name}---")
                subscriber!!.onNext(1)
                subscriber.onCompleted()
            })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ i ->
                        Log.d(TAG, "Subscriber#observeOn: ----Current Thread Name is ${Thread.currentThread().name}---\n" + "result = $i")
                    })

            // timeout  超时执行onError或备用Observable
            Observable.create(/*Observable.OnSubscribe<Long>*/ { subscriber: Subscriber<in Long>? ->
                for (i in 0L..4) {
                    try {
                        Thread.sleep(i * 100)
                    } catch (e: InterruptedException) {
                        Log.d(TAG, "中断异常：e = ${e.message}")
                    }
                    subscriber!!.onNext(i)
                }
                subscriber!!.onCompleted()
            }).timeout(200, TimeUnit.MILLISECONDS, Observable.just(10L, 11)) // 超时执行备胎
                    .subscribe({ i ->
                        Log.d(TAG, "timeout: $i")
                    })

        }

        /**
         * 组合操作符调用
         */
        fun mergeOperatorCall() {
            // startWith
            Observable.from(arrayOf(11, 12, 13))
                    .startWith(1, 2)
                    .subscribe({ i ->
                        Log.d(TAG, "startWith: 源数据前插入数据 $i")
                    })
            // merge，结果导向，谁快谁先发送
            Observable.merge(
                    Observable.just(1, 2, 3).subscribeOn(Schedulers.io()),// 在io线程执行
                    Observable.just(4, 5, 6)) // 主线程
                    .subscribe({ i ->
                        Log.d(TAG, "merge: 合并多个被监测对象，顺序可能颠倒  $i")
                    })
            // concat，规则导向，排队发送
            Observable.concat(
                    Observable.just(1, 2, 3).subscribeOn(Schedulers.io()),// 在io线程执行
                    Observable.just(4, 5, 6),  // main thread
                    Observable.just(7, 8, 9))
                    .subscribe({ i ->
                        Log.d(TAG, "concat: 合并多个被监测对象，顺序可能颠倒  $i")
                    })
            // zip, 合并两个以上的数据，并作类型转换
            Observable.zip(
                    Observable.just(1, 2, 3),
                    Observable.just("a", "b", "c"),  // main thread
                    Observable.just(7L, 8L, 9L),
                    Func3<Int, String, Long, String>() { i, s, l ->
                        i.toString() + s + l
                    }).subscribe({ result ->
                Log.d(TAG, "zip: 合并多个数据，并做类型转换输出，result = $result")
            })
            // combineLatest 先触发的observable的最新发射数据，与后触发的observable组合
            Observable.combineLatest(
                    Observable.just(1, 2, 3),
                    Observable.just("a", "b", "c"),  // main thread
                    { i, s ->
                        i.toString() + s
                    }).subscribe({ result ->
                Log.d(TAG, "combineLatest: 组合最新的数据，返回结果result = $result")
            })
        }

        /**
         * 筛选操作符调用
         */
        fun filterOperatorCall() {
            // filter
            Observable.just(1, 2, 3, 4)
                    .filter(Func1<Int, Boolean>() { integer ->
                        integer > 2
                    })
                    .subscribe(Action1 { i ->
                        Log.d(TAG, "filter: 大于2的值 $i")
                    })
            // elementAt
            Observable.from(arrayOf(1, 2, 3, 4))
                    .elementAtOrDefault(12, 0)
                    .subscribe(
                            { i ->
                                Log.d(TAG, "elementAt: 指定位置的值 $i")
                            }
                    )
            // distinct 去重
            Observable.from(arrayOf(1, 2, 2, 3, 3, 2, 4))
//                            .distinct() // 去重，注意：操作符同时使用时，只是有距Observable最近的那一个
                    .distinctUntilChanged() // 去连重
                    .subscribe({ i ->
                        Log.d(TAG, "distinct: 去重复后 $i")
                    })
            // skip\take
            Observable.from(arrayOf(1, 2, 3, 4, 5, 6))
                    .take(2) // 取前2项
                    .subscribe({ i ->
                        Log.d(TAG, "skip: 取前n项，$i")
                    })
            Observable.from(arrayOf(1, 2, 3, 4, 5, 6))
                    .skip(2) // 跳过前2项
                    .subscribe({ i ->
                        Log.d(TAG, "skip: 跳过前n项，$i")
                    })
            // ignoreElements
            Observable.from(arrayOf(1, 2, 3, 4))
                    .ignoreElements()
                    .subscribe(object : Observer<Int> { // lambda表达式不适用于多个方法的接口,要用object显式声明
                        override fun onCompleted() {
                            Log.d(TAG, "监测结果被忽略，执行了 onComplete()")
                        }

                        override fun onError(e: Throwable?) {
                            Log.d(TAG, "监测结果被忽略，执行了 onError() e = ${e!!.message}")
                        }

                        override fun onNext(t: Int?) {
                            Log.d(TAG, "监测结果被忽略，不执行 onNext()")
                        }
                    })
            // throttleFirst
            Observable.create(Observable.OnSubscribe<Int> { subscriber ->
                for (i in 0..10) {
                    subscriber.onNext(i)
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        Log.d(TAG, "e = $e")
                    }
                }
                subscriber.onCompleted()
            }).throttleFirst(200, TimeUnit.MILLISECONDS) // 每200ms取当前第一个被监测到的结果发送出去
                    .subscribe({ i ->
                        Log.d(TAG, "throttleFirst: 每200ms接收到一个结果，且是第一个被监测到的结果 $i")
                    })
            // throttleWithTimeOut
            Observable.create(Observable.OnSubscribe<Int> { subscriber: Subscriber<in Int>? ->
                for (i in 0..9) {
                    subscriber!!.onNext(i)
                    var sleep = 100L
                    if (i % 3 == 0) {
                        sleep = 300L
                    }
                    try {
                        Thread.sleep(sleep)
                    } catch (e: InterruptedException) {
                        Log.d(TAG, "e = $e")
                    }
                }
                subscriber!!.onCompleted()
            }).throttleWithTimeout(200, TimeUnit.MILLISECONDS).subscribe(
                    { i ->
                        Log.d(TAG, "throttleWithTimeout,发送结果200ms以内的数据被忽略，超过200ms的数据被接收: $i")
                    }
            )
        }

        /**
         * 变换操作符调用
         * 注意：跟转换toMap操作符的区分
         */
        fun mapOperatorCall() {
            val host = "http://blog.csdn.net/"
            // map
            Observable.just("sinat_25074703")
                    // 变换被监测值
                    .map(Func1<String, String>() { s ->
                        host + s
                    }).subscribe({ s ->
                Log.d(TAG, "map: " + s)
            })
            // flatMap and cast
            val list = ArrayList<String>()
            list.add("sinat_25074704")
            list.add("sinat_25074705")
            list.add("sinat_25074706")
            Observable.from(list)
                    // 将string转化为Observable<String>
                    .flatMap(Func1<String, Observable<String>> { s ->
                        run {
                            Log.d(TAG, "flatMap: 允许不按顺序输出")
                            Observable.just(host + s)
                        }
                    })
                    // 强转为指定类型
                    .cast(String::class.java).subscribe({ s ->
                // 注意这里的转换，Java中String.class
                Log.d(TAG, "flatMap: $s")
            })
            // concatMap，类似于flatMap，有序发射监测结果
            Observable.from(list)
                    .concatMap({ s ->
                        Observable.just(host + s)
                    })
                    .cast(String::class.java).subscribe({ s ->
                Log.d(TAG, "concatMap: $s")
            })
            // flatMapIterable
            Observable.just(1, 2, 3)
                    // 将被监测数据统一打包到list一次发送
                    .flatMapIterable<Any>(Func1<Int, MutableList<Int>> { integer ->
                        @Suppress("NAME_SHADOWING")
                        val list = ArrayList<Int>()
                        list.add(integer!! + 100)
                        list
                    }).subscribe({ integer -> Log.d(TAG, "flatMapIterable:" + integer!!) })
            // buffer
            Observable.just(1, 2, 3, 4, 5, 6)
                    .buffer(3) // 每次发射三个观测结果
                    .subscribe(/*Action1<MutableList<Int>>()*/ { integers ->
                        run {
                            for (i in integers) {
                                Log.d(TAG, "buffer: " + i)
                            }
                            Log.d(TAG, "----------------")
                        }
                    })
            // groupBy
            val observable: Observable<GroupedObservable<String, SwordMan>> =
                    Observable.just(SwordMan("韦一笑", "A"),
                            SwordMan("张三丰", "SS"),
                            SwordMan("韦一笑2", "S"),
                            SwordMan("韦一笑3", "SS"),
                            SwordMan("韦一笑4", "A"),
                            SwordMan("韦一笑5", "S"))
                            .groupBy {  // 根据level分组创建observable实例发送
                                swordMan: SwordMan? ->
                                swordMan!!.level
                            }
            Observable.concat(observable).subscribe(Action1<SwordMan>() { swordMan: SwordMan? ->
                Log.d(TAG, "groupBy: " + swordMan!!.name + "---" + swordMan.level)
            })
        }

        /**
         * 创建observable操作符调用
         */
        fun createOperatorCall() {
            // create 创建 observable
            val observable: Observable<String> = Observable.create {
                it.onNext("first")
                it.onNext("second")
                it.onNext("third")
                it.onCompleted()
            }
            val observer: Observer<String> = object : Observer<String> { // lambda表达式不适用于多个方法的接口
                override fun onNext(t: String?) {
                    Log.d(TAG, "$t time execute onNext ")
                }

                override fun onCompleted() {
                    Log.d(TAG, "All Completed")
                }

                override fun onError(e: Throwable?) {
                    Log.d(TAG, "Error msg = ${e!!.message}")
                }
            }
            observable.subscribe(observer)

            // just 创建 observable
            val observableJust: Observable<String> = Observable.just("just: first", "just: second")
            val subscriber: Subscriber<String> = object : Subscriber<String>() {
                override fun onStart() {
                    Log.d(TAG, "subscriber need execute onStart ")
                }

                override fun onNext(t: String?) {
                    Log.d(TAG, "$t time execute onNext ")
                }

                override fun onCompleted() {
                    Log.d(TAG, "All Completed")
                }

                override fun onError(e: Throwable?) {
                    Log.d(TAG, "Error msg = ${e!!.message}")
                }
            }
            observableJust.subscribe(subscriber)

            // from 创建 observable
            val observableFrom: Observable<String> = Observable.from(arrayOf("from: first", "from: second"))
            observableFrom.subscribe(observer) // 使用subscriber无效

            // 每隔3s执行一次当前作用域，也就是call方法
            Observable.interval(3, TimeUnit.MINUTES)
                    .subscribe({ t: Long? ->
                        run {
                            Log.d(TAG, "interval:" + t?.toInt())
                        }
                    })

            // 连续调用5次当前作用域，也就是call方法
            Observable.range(0, 5)
                    .subscribe({ i: Int? ->
                        run {
                            Log.d(TAG, "range:" + i)
                        }
                    })

            // 连续2次执行range功能，当前作用域执行6次，也就是call方法。
            Observable.range(0, 3)
                    .repeat(2)
                    .subscribe({ integer: Int? ->
                        Log.d(TAG, "repeat: " + integer)
                    })
        }
    }
}