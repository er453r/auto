package com.er453r.auto.job

import com.er453r.auto.image.ImageUtils
import com.er453r.auto.pipeline.Pipeline
import com.er453r.auto.pipeline.PipelineRepository
import com.er453r.auto.pipeline.job.Job
import com.er453r.auto.pipeline.job.JobLogic
import com.er453r.auto.pipeline.job.JobRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JobTests {
    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var imageUtils: ImageUtils

    @Autowired
    lateinit var pipelineRepository: PipelineRepository

    @Autowired
    lateinit var jobRepository: JobRepository

    @Autowired
    lateinit var jobLogic: JobLogic

    @Test
    fun `Basic Job Test`() {
        imageUtils.addDefault()

        val pipeline = Pipeline(
            env = mapOf(
                "GIT_SSH" to "git@github.com:er453r/autodash.git",
                "GIT_KEY" to "-----BEGIN RSA PRIVATE KEY-----\n" +
                        "MIICXAIBAAKBgQC2l4dd2rhXEeK2dmKwEwK2TbvEfGAj/yCPdQQljQ9cQE7jHp9W\n" +
                        "dSQBMUhUhqnj3LzEim39JKGVxdKKib8723vJgcxMYThwhUGnyUV25dX67qQUZ858\n" +
                        "iBrNCw2aX4SA3FQhz0MNa29ab+LcZXr3ptNGqXV3HBo6+L7QqsuC3K3PrQIDAQAB\n" +
                        "AoGAVPp+2LJjmrpLPkoosfOXDots+Qw9nmMtnzaBoIxe+rs/O7rTw2QvRpLOK/Ck\n" +
                        "ftUL0ZjviaITxeXUj/99zfjfSeWxlOa9zqcb7AzDkSvUg0/dHCWSpIHcj0Ulc84R\n" +
                        "NlX/E9au3OHGAjeaEfgBdn0ZmKUiBlDmSlWG+nM8tk3HRu0CQQDxfhy6wqKRG748\n" +
                        "pcsgEqD1Bj/xcF9dfnKLiAS764HsljBmDzIq2ZMgeO9oYR7iJ1zeKoew1WHN7wRe\n" +
                        "cG2MqVQnAkEAwY+WztVw+7jorda0nZZwPS2QFf2bzoU2YoJLeQCORfP/E7h9Kv2I\n" +
                        "88ZWjqsetCt7r7lTqMTb413PhPmIh6R+CwJAP5qUnIfY41koQLb2Cet+IyrTOpb7\n" +
                        "NxevuZpIjOoZXSOteaP09Z46hunzPE2gIbZ2WjlfZa69xMx4HRuFbu/GLwJASZRc\n" +
                        "/rlSns4W3WZ9+F3kwszcNg/XN7WaC+595Dya2Oq873E1W69PbWPbKZbRMCpLdWxc\n" +
                        "6g4TjKXvqhv1k6KrKwJBAN61S3y34MCBDF9cI2e4b7DO/87odfU3EZXGOUQmD5B3\n" +
                        "mzv219Tt0fY19pRzpyQ/Vm9lFDJCoLACC1op2trRXqE=\n" +
                        "-----END RSA PRIVATE KEY-----",
//                "CHECKOUT" to "6d2d491", // test explicit checkout
//                "IGNORE_LAST_COMMIT" to "true",
            ),
        )

        pipelineRepository.save(pipeline)

        val job = Job(
            pipeline = pipeline,
            env = pipeline.env,
        )

        jobRepository.save(job)

        jobLogic.run(
            job = job,
        )

        while (job.status != Job.Status.DONE) {
            Thread.sleep(1000)
        }

        logger.info { "Test finished!" }
    }
}
