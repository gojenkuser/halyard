/*
 * Copyright 2017 Microsoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.halyard.cli.command.v1.config.persistentStorage.s3;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.netflix.spinnaker.halyard.cli.command.v1.config.persistentStorage.AbstractPersistentStoreEditCommand;
import com.netflix.spinnaker.halyard.cli.command.v1.config.providers.aws.AwsCommandProperties;
import com.netflix.spinnaker.halyard.cli.ui.v1.AnsiUi;
import com.netflix.spinnaker.halyard.config.model.v1.node.PersistentStore;
import com.netflix.spinnaker.halyard.config.model.v1.persistentStorage.S3PersistentStore;

import java.util.UUID;

@Parameters(separators = "=")
public class S3EditCommand extends AbstractPersistentStoreEditCommand<S3PersistentStore> {
  protected String getPersistentStoreType() {
    return PersistentStore.PersistentStoreType.S3.getId();
  }

  @Parameter(
    names = "--bucket",
    description = "The name of a storage bucket that your specified account has access to. If not "
      + "specified, a random name will be chosen. If you specify a globally unique bucket name "
      + "that doesn't exist yet, Halyard will create that bucket for you."
  )
  private String bucket;

  @Parameter(
    names = "--root-folder",
    description = "The root folder in the chosen bucket to place all of Spinnaker's persistent data in."
  )
  private String rootFolder;

  @Parameter(
    names = "--region",
    description = "This is only required if the bucket you specify doesn't exist yet. In that case, the "
      + "bucket will be created in that region. See http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region."
  )
  private String region;

  @Parameter(
    names = "--assume-role",
    description = AwsCommandProperties.ASSUME_ROLE_DESCRIPTION
  )
  private String assumeRole;

  @Parameter(
    names = "--access-key-id",
    description = AwsCommandProperties.ACCESS_KEY_ID_DESCRIPTION
  )
  private String accessKeyId;

  @Parameter(
    names = "--secret-access-key",
    description = AwsCommandProperties.SECRET_KEY_DESCRIPTION,
    password = true
  )
  private String secretAccessKey;

  @Override
  protected S3PersistentStore editPersistentStore(S3PersistentStore persistentStore) {
    persistentStore.setBucket(isSet(bucket) ? bucket : persistentStore.getBucket());
    persistentStore.setRootFolder(isSet(rootFolder) ? rootFolder : persistentStore.getRootFolder());
    persistentStore.setRegion(isSet(region) ? region : persistentStore.getRegion());
    persistentStore.setAccessKeyId(isSet(accessKeyId) ? accessKeyId : persistentStore.getAccessKeyId());
    persistentStore.setSecretAccessKey(isSet(secretAccessKey) ? secretAccessKey : persistentStore.getSecretAccessKey());

    if (persistentStore.getBucket() == null) {
      String bucketName = "spin-" + UUID.randomUUID().toString();
      AnsiUi.raw("Generated bucket name: " + bucketName);
      persistentStore.setBucket(bucketName);
    }

    return persistentStore;
  }
}
