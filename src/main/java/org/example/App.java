// Copyright 2022 Google LLC
//
// Licensed under the Apache License, Version 2.0 <LICENSE-APACHE or
// https://www.apache.org/licenses/LICENSE-2.0> or the MIT license
// <LICENSE-MIT or https://opensource.org/licenses/MIT>, at your
// option. This file may not be copied, modified, or distributed
// except according to those terms.

package org.example;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);


    public interface ReadTopicPubSubOptions extends PipelineOptions {
        @Description("Topic to read from")
        @Default.String("projects/pubsub-public-data/topics/taxirides-realtime")
        String getTopic();

        void setTopic(String value);
    }

    public static PCollection<String> buildPipeline(Pipeline pipeline, String topic) {
        return pipeline
                // Reading from a Topic creates a subscription to it automatically
                .apply("ReadFromPubSub", PubsubIO.readStrings().fromTopic(topic))
                // Next ParDo is added so pipeline doesn't finish
                .apply("ParDo", ParDo.of(new DoFn<String, String>() {
                            @ProcessElement
                            public void processElement(ProcessContext c) {
                                c.output(c.element());
                            }
                        })
                )
                .apply("Print elements",
                        MapElements.into(TypeDescriptors.strings()).via(x -> {
                            LOG.info(x);
                            return x;
                        }));
    }

    public static void main(String[] args) {
        ReadTopicPubSubOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(ReadTopicPubSubOptions.class);
        Pipeline pipeline = Pipeline.create(options);
        App.buildPipeline(pipeline, options.getTopic());
        pipeline.run();
    }
}