/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.eureka2.server.service;

import com.netflix.eureka2.interests.ChangeNotification;
import com.netflix.eureka2.interests.Interest;
import com.netflix.eureka2.registry.instance.InstanceInfo;
import com.netflix.eureka2.channel.InterestChannel;
import rx.Observable;
import rx.subjects.PublishSubject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Stub implementation of {@link InterestChannel} suitable for unit testing.
 *
 * @author Tomasz Bak
 */
public class TestableInterestChannel implements InterestChannel {

    private final BlockingQueue<Object> updateQueue = new LinkedBlockingQueue<Object>();

    private PublishSubject<ChangeNotification<InstanceInfo>> notificationsObservable = PublishSubject.create();

    private PublishSubject<Void> closeObservable = PublishSubject.create();

    @Override
    public Observable<Void> change(Interest<InstanceInfo> newInterest) {
        updateQueue.add(newInterest);
        return Observable.empty();
    }

    @Override
    public void close() {
        closeObservable.onCompleted();
    }

    @Override
    public Observable<Void> asLifecycleObservable() {
        return null;
    }

    /*
     * Test methods.
     */

    public void submitNotification(ChangeNotification<InstanceInfo> notification) {
        notificationsObservable.onNext(notification);
    }

    /**
     * Return observable that completes when the channel is closed.
     */
    public Observable<Void> viewClose() {
        return closeObservable;
    }
}
